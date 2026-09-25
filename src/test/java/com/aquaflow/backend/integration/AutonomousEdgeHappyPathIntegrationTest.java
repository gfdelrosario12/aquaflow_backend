package com.aquaflow.backend.integration;

import com.aquaflow.backend.domain.AwdConfigService;
import com.aquaflow.backend.domain.IrrigationCommandStateMachine;
import com.aquaflow.backend.domain.IrrigationDecisionService;
import com.aquaflow.backend.domain.TelemetryIngestionService;
import com.aquaflow.backend.dto.request.AutoIrrigationConfigRequest;
import com.aquaflow.backend.dto.request.IrrigationDecisionRequest;
import com.aquaflow.backend.dto.request.TelemetryUplinkRequest;
import com.aquaflow.backend.dto.response.AutoIrrigationConfigResponse;
import com.aquaflow.backend.dto.response.IrrigationDecisionResponse;
import com.aquaflow.backend.dto.response.TelemetryUplinkResponse;
import com.aquaflow.backend.entity.*;
import com.aquaflow.backend.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AutonomousEdgeHappyPathIntegrationTest {

    @Autowired
    private TelemetryIngestionService telemetryIngestionService;

    @Autowired
    private IrrigationDecisionService irrigationDecisionService;

    @Autowired
    private AwdConfigService awdConfigService;

    @Autowired
    private IrrigationCommandStateMachine irrigationCommandStateMachine;

    @Autowired
    private EdgeNodeRepository edgeNodeRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private MonitoringZoneRepository monitoringZoneRepository;

    @Autowired
    private MonitoringPointRepository monitoringPointRepository;

    @Autowired
    private TelemetryReadingRepository telemetryReadingRepository;

    @Autowired
    private IrrigationDecisionRepository irrigationDecisionRepository;

    private EdgeNode node;
    private Field field;
    private MonitoringZone zone;

    @BeforeEach
    void setUp() {
        field = fieldRepository.save(Field.builder()
                .name("Happy Path Test Field")
                .boundaryGeoJson("{\"type\":\"Polygon\",\"coordinates\":[]}")
                .areaHectares(12.0)
                .build());

        zone = monitoringZoneRepository.save(MonitoringZone.builder()
                .name("Zone Happy")
                .field(field)
                .cropType("RICE")
                .targetMoisturePercentage(85.0)
                .waterAllocationLimitLiters(15000.0)
                .build());

        node = edgeNodeRepository.save(EdgeNode.builder()
                .nodeId("HAPPY-NODE-001")
                .identity(CommunicationIdentity.builder()
                        .identityType(CommunicationIdentityType.LORA_DEVEUI)
                        .identityValue("0004A30B001C8C99")
                        .build())
                .lifecycleState(NodeLifecycleState.ACTIVE)
                .healthState(HealthState.HEALTHY)
                .healthMetrics(NodeHealthMetrics.builder()
                        .batteryLevel(98.0)
                        .signalDbm(-70)
                        .consecutiveFailures(0)
                        .lastHeartbeat(LocalDateTime.now())
                        .lastTelemetryAt(LocalDateTime.now())
                        .isTelemetryStale(false)
                        .build())
                .monitoringZone(zone)
                .hardwareModel("ESP32-LoRa-V2")
                .firmwareVersion("v1.2.0")
                .build());

        monitoringPointRepository.save(MonitoringPoint.builder()
                .name("MP-HAPPY-001")
                .monitoringZone(zone)
                .edgeNode(node)
                .primarySensorType(SensorType.SOIL_MOISTURE)
                .depthCm(15.0)
                .build());
    }

    @Test
    void testEndToEndAutonomousEdgeHappyPath() {
        // 1. ESP32 Telemetry Uplink Ingestion
        TelemetryUplinkRequest uplink = TelemetryUplinkRequest.builder()
                .nodeId("HAPPY-NODE-001")
                .batteryLevel(95.0)
                .rssiDbm(-72)
                .snrDb(10.0)
                .timestamp(LocalDateTime.now())
                .measurements(Map.of(
                        "SOIL_MOISTURE", 82.0
                ))
                .build();

        TelemetryUplinkResponse uplinkResponse = telemetryIngestionService.ingestUplink(uplink);
        assertThat(uplinkResponse).isNotNull();
        assertThat(uplinkResponse.isSuccess()).isTrue();
        assertThat(telemetryReadingRepository.findByEdgeNodeIdOrderByTimestampDesc(node.getId())).hasSize(1);

        // 2. Edge Autonomous Decision Reporting (Edge-driven, Zero Cloud Decision)
        IrrigationDecisionRequest decisionRequest = IrrigationDecisionRequest.builder()
                .edgeNodeId(node.getId())
                .decisionType(DecisionType.SOIL_MOISTURE_TRIGGER)
                .triggerReason(TriggerReason.SOIL_MOISTURE_BELOW_MIN)
                .cropStage(CropGrowthStage.VEGETATIVE)
                .confidence(0.95)
                .requestedDurationMinutes(30)
                .requestedVolumeLiters(300.0)
                .executionStatus("IN_PROGRESS")
                .build();

        IrrigationDecisionResponse decisionResponse = irrigationDecisionService.reportDecision(decisionRequest);
        assertThat(decisionResponse).isNotNull();
        assertThat(decisionResponse.getEdgeNodeId()).isEqualTo(node.getId());
        List<IrrigationDecision> decisions = irrigationDecisionRepository.findByEdgeNodeId(node.getId());
        assertThat(decisions).hasSize(1);

        // 3. Cloud AWD Configuration Update & Downlink Task Generation
        AutoIrrigationConfigRequest configRequest = AutoIrrigationConfigRequest.builder()
                .enabled(true)
                .maxDurationMinutes(60)
                .targetFloodDepthCm(15.0)
                .build();

        AutoIrrigationConfigResponse configResponse = awdConfigService.updateAwdConfig(field.getId(), configRequest);
        assertThat(configResponse).isNotNull();
        assertThat(configResponse.getEnabled()).isTrue();

        // 4. Command State Machine Transition Execution
        String correlationId = UUID.randomUUID().toString();
        IrrigationCommandRecord command = irrigationCommandStateMachine.registerCommand(
                correlationId, "MANUAL_START", field.getId(), node.getId(), "OPERATOR", "Manual start test"
        );
        assertThat(command.getCurrentState()).isEqualTo(CommandState.ACCEPTED);

        command = irrigationCommandStateMachine.transitionState(correlationId, CommandState.QUEUED, "OPERATOR", "Queued", null, "op1", "OPERATOR");
        assertThat(command.getCurrentState()).isEqualTo(CommandState.QUEUED);

        command = irrigationCommandStateMachine.transitionState(correlationId, CommandState.DOWNLINK_TRANSMITTED, "SYSTEM", "Sent via LoRaWAN", null, null, null);
        assertThat(command.getCurrentState()).isEqualTo(CommandState.DOWNLINK_TRANSMITTED);

        command = irrigationCommandStateMachine.transitionState(correlationId, CommandState.EDGE_ACKNOWLEDGED, "EDGE_NODE", "Node ACK received", "{\"rssi\":-80}", null, null);
        assertThat(command.getCurrentState()).isEqualTo(CommandState.EDGE_ACKNOWLEDGED);

        command = irrigationCommandStateMachine.transitionState(correlationId, CommandState.EXECUTING, "EDGE_NODE", "Valve opening", null, null, null);
        assertThat(command.getCurrentState()).isEqualTo(CommandState.EXECUTING);

        command = irrigationCommandStateMachine.transitionState(correlationId, CommandState.COMPLETED, "EDGE_NODE", "Duration elapsed", null, null, null);
        assertThat(command.getCurrentState()).isEqualTo(CommandState.COMPLETED);
    }
}
