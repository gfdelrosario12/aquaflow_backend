package com.aquaflow.backend.integration;

import com.aquaflow.backend.domain.*;
import com.aquaflow.backend.dto.request.AutoIrrigationConfigRequest;
import com.aquaflow.backend.dto.request.AwdThresholdConfigRequest;
import com.aquaflow.backend.dto.response.AutoIrrigationConfigResponse;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ConfigSyncDownlinkPipelineIntegrationTest {

    @Autowired
    private AwdConfigService awdConfigService;

    @Autowired
    private EdgeNodeSyncService edgeNodeSyncService;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private MonitoringZoneRepository monitoringZoneRepository;

    @Autowired
    private EdgeNodeRepository edgeNodeRepository;

    @Autowired
    private DownlinkQueueItemRepository downlinkQueueItemRepository;

    @Autowired
    private ConfigSyncTaskRepository syncTaskRepository;

    private Field field;
    private MonitoringZone zone;
    private EdgeNode node;

    @BeforeEach
    void setUp() {
        field = fieldRepository.save(Field.builder()
                .name("Config Sync Test Field")
                .boundaryGeoJson("{\"type\":\"Polygon\",\"coordinates\":[]}")
                .areaHectares(5.0)
                .build());

        zone = monitoringZoneRepository.save(MonitoringZone.builder()
                .name("Zone Config Beta")
                .field(field)
                .cropType("RICE")
                .targetMoisturePercentage(85.0)
                .waterAllocationLimitLiters(5000.0)
                .build());

        node = edgeNodeRepository.save(EdgeNode.builder()
                .nodeId("NODE-CFG-002")
                .identity(CommunicationIdentity.builder()
                        .identityType(CommunicationIdentityType.LORA_DEVEUI)
                        .identityValue("0004A30B001C8C02")
                        .build())
                .lifecycleState(NodeLifecycleState.ACTIVE)
                .healthState(HealthState.HEALTHY)
                .healthMetrics(NodeHealthMetrics.builder()
                        .batteryLevel(98.0)
                        .lastHeartbeat(LocalDateTime.now())
                        .build())
                .monitoringZone(zone)
                .hardwareModel("ESP32-LoRa-V2")
                .firmwareVersion("v1.2.0")
                .build());
    }

    @Test
    void testConfigSync_QueuesDownlink_And_ProcessesAck_CompletesStateMachine() {
        AutoIrrigationConfigRequest updateRequest = AutoIrrigationConfigRequest.builder()
                .enabled(true)
                .maxDurationMinutes(60)
                .minCooldownMinutes(120)
                .allowedStartHour(6)
                .allowedEndHour(18)
                .targetFloodDepthCm(5.0)
                .minConfidenceThreshold(0.85)
                .updatedBy("operator-admin")
                .changeReason("Seasonal threshold adjust")
                .thresholds(List.of(
                        AwdThresholdConfigRequest.builder()
                                .growthStage(CropGrowthStage.VEGETATIVE)
                                .triggerMoisturePercentage(60.0)
                                .targetMoisturePercentage(85.0)
                                .targetFloodDepthCm(5.0)
                                .build()
                ))
                .build();

        // 1. Update config
        AutoIrrigationConfigResponse configResponse = awdConfigService.updateAwdConfig(field.getId(), updateRequest);
        assertThat(configResponse).isNotNull();
        assertThat(configResponse.getEnabled()).isTrue();

        // 2. Trigger sync
        edgeNodeSyncService.syncConfigForField(field.getId());

        // 3. Verify queue item created
        List<DownlinkQueueItem> queueItems = downlinkQueueItemRepository.findByEdgeNodeIdAndStatus(node.getId(), ConfigSyncStatus.QUEUED);
        assertThat(queueItems).isNotEmpty();
        DownlinkQueueItem queueItem = queueItems.get(0);
        String correlationId = queueItem.getCorrelationId();

        // 4. Handle Edge ACK
        edgeNodeSyncService.processAcknowledgement(correlationId);

        // 5. Verify ACK status updated
        DownlinkQueueItem ackedItem = downlinkQueueItemRepository.findByCorrelationId(correlationId).orElseThrow();
        assertThat(ackedItem.getStatus()).isEqualTo(ConfigSyncStatus.ACKNOWLEDGED);

        ConfigSyncTask completedTask = syncTaskRepository.findByCorrelationId(correlationId).orElseThrow();
        assertThat(completedTask.getStatus()).isEqualTo(ConfigSyncStatus.ACKNOWLEDGED);
    }
}

