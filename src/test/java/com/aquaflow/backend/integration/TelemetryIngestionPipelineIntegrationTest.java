package com.aquaflow.backend.integration;

import com.aquaflow.backend.domain.NodeHealthMonitor;
import com.aquaflow.backend.domain.TelemetryFreshnessChecker;
import com.aquaflow.backend.domain.TelemetryIngestionService;
import com.aquaflow.backend.dto.request.TelemetryUplinkRequest;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TelemetryIngestionPipelineIntegrationTest {

    @Autowired
    private TelemetryIngestionService telemetryIngestionService;

    @Autowired
    private NodeHealthMonitor nodeHealthMonitor;

    @Autowired
    private TelemetryFreshnessChecker telemetryFreshnessChecker;

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

    private EdgeNode node;
    private Field field;
    private MonitoringZone zone;

    @BeforeEach
    void setUp() {
        field = fieldRepository.save(Field.builder()
                .name("Integration Test Field")
                .boundaryGeoJson("{\"type\":\"Polygon\",\"coordinates\":[]}")
                .areaHectares(10.5)
                .build());

        zone = monitoringZoneRepository.save(MonitoringZone.builder()
                .name("Zone Alpha")
                .field(field)
                .cropType("RICE")
                .targetMoisturePercentage(80.0)
                .waterAllocationLimitLiters(10000.0)
                .build());

        node = edgeNodeRepository.save(EdgeNode.builder()
                .nodeId("NODE-INT-001")
                .identity(CommunicationIdentity.builder()
                        .identityType(CommunicationIdentityType.LORA_DEVEUI)
                        .identityValue("0004A30B001C8C01")
                        .build())
                .lifecycleState(NodeLifecycleState.ACTIVE)
                .healthState(HealthState.HEALTHY)
                .healthMetrics(NodeHealthMetrics.builder()
                        .batteryLevel(95.0)
                        .signalDbm(-75)
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
                .name("MP-INT-001")
                .monitoringZone(zone)
                .edgeNode(node)
                .primarySensorType(SensorType.SOIL_MOISTURE)
                .depthCm(15.0)
                .build());
    }

    @Test
    void testTelemetryIngestion_UpdatesReadingsHealthAndPublishesEvents() {
        TelemetryUplinkRequest request = TelemetryUplinkRequest.builder()
                .nodeId("NODE-INT-001")
                .batteryLevel(92.0)
                .rssiDbm(-78)
                .snrDb(9.5)
                .timestamp(LocalDateTime.now())
                .measurements(Map.of(
                        "SOIL_MOISTURE", 78.0,
                        "TEMPERATURE", 28.5
                ))
                .build();

        TelemetryUplinkResponse response = telemetryIngestionService.ingestUplink(request);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getReadingsIngested()).isEqualTo(2);

        // Verify telemetry readings persisted
        List<TelemetryReading> readings = telemetryReadingRepository.findByEdgeNodeIdOrderByTimestampDesc(node.getId());
        assertThat(readings).hasSize(2);

        // Evaluate node health & freshness
        nodeHealthMonitor.evaluateNodeHealth();
        telemetryFreshnessChecker.checkTelemetryFreshness();

        EdgeNode updatedNode = edgeNodeRepository.findById(node.getId()).orElseThrow();
        assertThat(updatedNode.getHealthState()).isEqualTo(HealthState.HEALTHY);
        assertThat(updatedNode.getHealthMetrics().getIsTelemetryStale()).isFalse();
    }
}
