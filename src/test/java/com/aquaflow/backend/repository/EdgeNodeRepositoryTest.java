package com.aquaflow.backend.repository;

import com.aquaflow.backend.entity.*;
import com.aquaflow.backend.persistence.EdgeNodeRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EdgeNodeRepositoryTest {

    private final EdgeNodeRepository edgeNodeRepository = mock(EdgeNodeRepository.class);

    @Test
    void shouldCreateAndFindEdgeNode() {
        CommunicationIdentity identity = CommunicationIdentity.builder()
                .identityType(CommunicationIdentityType.MAC_ADDRESS)
                .identityValue("AA:BB:CC:DD:EE:FF")
                .macAddress("AA:BB:CC:DD:EE:FF")
                .serialNumber("SN-12345")
                .build();

        NodeHealthMetrics metrics = NodeHealthMetrics.builder()
                .batteryLevel(95.5)
                .solarVoltage(12.4)
                .signalDbm(-65)
                .lastHeartbeat(LocalDateTime.now())
                .build();

        EdgeNode node = EdgeNode.builder()
                .id(1L)
                .nodeId("EDGE-NODE-001")
                .identity(identity)
                .lifecycleState(NodeLifecycleState.ACTIVE)
                .healthState(HealthState.HEALTHY)
                .healthMetrics(metrics)
                .hardwareModel("ESP32-S3-AQUA")
                .firmwareVersion("v2.1.0")
                .version(0L)
                .createdAt(LocalDateTime.now())
                .build();

        when(edgeNodeRepository.save(any(EdgeNode.class))).thenReturn(node);
        when(edgeNodeRepository.findByNodeId("EDGE-NODE-001")).thenReturn(java.util.Optional.of(node));

        var saved = edgeNodeRepository.save(node);
        assertThat(saved.getNodeId()).isEqualTo("EDGE-NODE-001");
        assertThat(saved.getLifecycleState()).isEqualTo(NodeLifecycleState.ACTIVE);
        assertThat(saved.getHealthState()).isEqualTo(HealthState.HEALTHY);

        var found = edgeNodeRepository.findByNodeId("EDGE-NODE-001");
        assertThat(found).isPresent();
        assertThat(found.get().getIdentity().getMacAddress()).isEqualTo("AA:BB:CC:DD:EE:FF");
    }
}

