package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.EdgeNodeRegistryServiceImpl;
import com.aquaflow.backend.dto.request.*;
import com.aquaflow.backend.dto.response.*;
import com.aquaflow.backend.entity.*;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.aquaflow.backend.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EdgeNodeRegistryServiceImplTest {

    @Mock
    private EdgeNodeRepository edgeNodeRepository;

    @Mock
    private MonitoringZoneRepository monitoringZoneRepository;

    @Mock
    private MonitoringPointRepository monitoringPointRepository;

    @Mock
    private TelemetryReadingRepository telemetryReadingRepository;

    @Mock
    private AutoIrrigationConfigRepository autoIrrigationConfigRepository;

    @Mock
    private IrrigationAuditLogRepository auditLogRepository;

    private EdgeNodeRegistryServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new EdgeNodeRegistryServiceImpl(
                edgeNodeRepository,
                monitoringZoneRepository,
                monitoringPointRepository,
                telemetryReadingRepository,
                autoIrrigationConfigRepository,
                auditLogRepository
        );
    }

    @Test
    void shouldRegisterNodeSuccessfully() {
        RegisterNodeRequest request = RegisterNodeRequest.builder()
                .nodeId("NODE-001")
                .identityType(CommunicationIdentityType.LORA_DEVEUI)
                .identityValue("DEV-EUI-001")
                .hardwareModel("V1.0")
                .firmwareVersion("1.0.0")
                .build();

        EdgeNode saved = EdgeNode.builder()
                .id(1L)
                .nodeId("NODE-001")
                .lifecycleState(NodeLifecycleState.PROVISIONED)
                .healthState(HealthState.HEALTHY)
                .identity(CommunicationIdentity.builder().identityType(CommunicationIdentityType.LORA_DEVEUI).identityValue("DEV-EUI-001").build())
                .build();

        when(edgeNodeRepository.findByNodeId("NODE-001")).thenReturn(Optional.empty());
        when(edgeNodeRepository.save(any(EdgeNode.class))).thenReturn(saved);

        EdgeNodeResponse response = service.registerNode(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNodeId()).isEqualTo("NODE-001");
        assertThat(response.getLifecycleState()).isEqualTo(NodeLifecycleState.PROVISIONED);
    }

    @Test
    void shouldThrowExceptionWhenRegisteringDuplicateNode() {
        RegisterNodeRequest request = RegisterNodeRequest.builder().nodeId("NODE-001").build();
        when(edgeNodeRepository.findByNodeId("NODE-001")).thenReturn(Optional.of(EdgeNode.builder().id(1L).build()));

        assertThatThrownBy(() -> service.registerNode(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void shouldCommissionNodeSuccessfully() {
        EdgeNode node = EdgeNode.builder()
                .id(1L)
                .nodeId("NODE-001")
                .lifecycleState(NodeLifecycleState.PROVISIONED)
                .build();

        CommissionNodeRequest request = CommissionNodeRequest.builder().txPowerDbm(14).intervalSeconds(300).build();

        when(edgeNodeRepository.findById(1L)).thenReturn(Optional.of(node));
        when(edgeNodeRepository.save(any(EdgeNode.class))).thenAnswer(i -> i.getArgument(0));

        CommissionNodeResponse response = service.commissionNode(1L, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getLifecycleState()).isEqualTo(NodeLifecycleState.COMMISSIONED);
        assertThat(response.getSingleUseCommissioningToken()).isNotBlank();
        verify(auditLogRepository, times(1)).save(any(IrrigationAuditLog.class));
    }

    @Test
    void shouldThrowExceptionWhenCommissioningDecommissionedNode() {
        EdgeNode node = EdgeNode.builder()
                .id(1L)
                .nodeId("NODE-001")
                .lifecycleState(NodeLifecycleState.DECOMMISSIONED)
                .build();

        when(edgeNodeRepository.findById(1L)).thenReturn(Optional.of(node));

        assertThatThrownBy(() -> service.commissionNode(1L, null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Cannot commission node");
    }

    @Test
    void shouldDecommissionNodeAndUnassignMonitoringPoints() {
        EdgeNode node = EdgeNode.builder()
                .id(1L)
                .nodeId("NODE-001")
                .lifecycleState(NodeLifecycleState.COMMISSIONED)
                .build();

        MonitoringPoint point = MonitoringPoint.builder().id(10L).edgeNode(node).build();

        when(edgeNodeRepository.findById(1L)).thenReturn(Optional.of(node));
        when(monitoringPointRepository.findByEdgeNodeId(1L)).thenReturn(List.of(point));
        when(edgeNodeRepository.save(any(EdgeNode.class))).thenAnswer(i -> i.getArgument(0));

        EdgeNodeResponse response = service.decommissionNode(1L);

        assertThat(response.getLifecycleState()).isEqualTo(NodeLifecycleState.DECOMMISSIONED);
        assertThat(point.getEdgeNode()).isNull();
        verify(monitoringPointRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldReplaceFaultyNodeSuccessfully() {
        EdgeNode faultyNode = EdgeNode.builder()
                .id(1L)
                .nodeId("FAULTY-001")
                .lifecycleState(NodeLifecycleState.COMMISSIONED)
                .build();

        EdgeNode replacementNode = EdgeNode.builder()
                .id(2L)
                .nodeId("REPLACE-002")
                .lifecycleState(NodeLifecycleState.PROVISIONED)
                .build();

        ReplaceNodeRequest request = ReplaceNodeRequest.builder()
                .replacementNodeId(2L)
                .transferMonitoringPoints(true)
                .build();

        when(edgeNodeRepository.findById(1L)).thenReturn(Optional.of(faultyNode));
        when(edgeNodeRepository.findById(2L)).thenReturn(Optional.of(replacementNode));
        when(edgeNodeRepository.save(any(EdgeNode.class))).thenAnswer(i -> i.getArgument(0));
        when(autoIrrigationConfigRepository.findByEdgeNodeId(1L)).thenReturn(Optional.empty());

        EdgeNodeResponse response = service.replaceNode(1L, request);

        assertThat(faultyNode.getLifecycleState()).isEqualTo(NodeLifecycleState.REPLACED);
        assertThat(replacementNode.getLifecycleState()).isEqualTo(NodeLifecycleState.COMMISSIONED);
        assertThat(response.getId()).isEqualTo(2L);
        verify(auditLogRepository, times(1)).save(any(IrrigationAuditLog.class));
    }

    @Test
    void shouldValidateNodeActiveForOperations() {
        EdgeNode activeNode = EdgeNode.builder()
                .id(1L)
                .nodeId("NODE-001")
                .lifecycleState(NodeLifecycleState.COMMISSIONED)
                .build();

        when(edgeNodeRepository.findById(1L)).thenReturn(Optional.of(activeNode));

        service.validateNodeActiveForOperations(1L); // Should not throw
    }

    @Test
    void shouldThrowExceptionWhenNodeIsInactiveForOperations() {
        EdgeNode decommissionedNode = EdgeNode.builder()
                .id(1L)
                .nodeId("NODE-001")
                .lifecycleState(NodeLifecycleState.DECOMMISSIONED)
                .build();

        when(edgeNodeRepository.findById(1L)).thenReturn(Optional.of(decommissionedNode));

        assertThatThrownBy(() -> service.validateNodeActiveForOperations(1L))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("NODE_NOT_ACTIVE");
    }

    @Test
    void shouldGetNodeHealth() {
        EdgeNode node = EdgeNode.builder()
                .id(1L)
                .nodeId("NODE-001")
                .healthState(HealthState.HEALTHY)
                .healthMetrics(NodeHealthMetrics.builder().batteryLevel(88.5).solarVoltage(5.1).signalDbm(-55).build())
                .build();

        when(edgeNodeRepository.findById(1L)).thenReturn(Optional.of(node));

        NodeHealthResponse response = service.getNodeHealth(1L);

        assertThat(response).isNotNull();
        assertThat(response.getBatteryLevel()).isEqualTo(88.5);
        assertThat(response.getHealthState()).isEqualTo(HealthState.HEALTHY);
    }
}

