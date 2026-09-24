package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.DownlinkQueueService;
import com.aquaflow.backend.domain.EmergencyStopServiceImpl;
import com.aquaflow.backend.dto.request.EmergencyStopRequest;
import com.aquaflow.backend.dto.response.CommandStatusResponse;
import com.aquaflow.backend.entity.*;
import com.aquaflow.backend.infrastructure.event.SystemEventPublisher;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.aquaflow.backend.persistence.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmergencyStopServiceImplTest {

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private EdgeNodeRepository edgeNodeRepository;

    @Mock
    private MonitoringZoneRepository monitoringZoneRepository;

    @Mock
    private DownlinkQueueService downlinkQueueService;

    @Mock
    private IrrigationAuditLogRepository auditLogRepository;

    @Mock
    private SystemEventPublisher systemEventPublisher;

    private ObjectMapper objectMapper;

    private EmergencyStopServiceImpl emergencyStopService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        emergencyStopService = new EmergencyStopServiceImpl(
                fieldRepository,
                edgeNodeRepository,
                monitoringZoneRepository,
                downlinkQueueService,
                auditLogRepository,
                systemEventPublisher,
                objectMapper
        );
    }

    @Test
    void shouldExecuteEmergencyStopForTargetFieldSuccessfully() {
        Field field = Field.builder().id(1L).name("Test Field").build();
        MonitoringZone zone = MonitoringZone.builder().id(10L).field(field).build();
        EdgeNode node = EdgeNode.builder().id(100L).nodeId("NODE-01").monitoringZone(zone).build();

        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
        when(monitoringZoneRepository.findByFieldId(1L)).thenReturn(List.of(zone));
        when(edgeNodeRepository.findByMonitoringZoneId(10L)).thenReturn(List.of(node));

        EmergencyStopRequest request = EmergencyStopRequest.builder()
                .operatorId("ADMIN-01")
                .fieldId(1L)
                .reason("Pipe burst detected in sector 1")
                .scope("FIELD")
                .build();

        CommandStatusResponse response = emergencyStopService.executeEmergencyStop(request);

        assertNotNull(response);
        assertNotNull(response.getCorrelationId());
        assertEquals(1L, response.getFieldId());
        assertEquals("ADMIN-01", response.getOperatorId());
        assertEquals(CommandLifecycleState.QUEUED, response.getStatus());
        assertEquals("EMERGENCY_STOP", response.getCommandType());

        verify(auditLogRepository, times(1)).save(any(IrrigationAuditLog.class));
        verify(downlinkQueueService, times(1)).queueDownlink(eq(node), anyString(), eq(response.getCorrelationId()));
        verify(systemEventPublisher, times(1)).publishEmergencyStop(eq(response.getCorrelationId()), eq(1L), eq("Pipe burst detected in sector 1"));
        verify(systemEventPublisher, times(1)).publishAlarm(eq(response.getCorrelationId()), eq(1L), eq("EMERGENCY_STOP"), eq("CRITICAL"), anyString());
    }

    @Test
    void shouldExecuteSystemWideEmergencyStop() {
        EdgeNode node1 = EdgeNode.builder().id(101L).nodeId("NODE-01").build();
        EdgeNode node2 = EdgeNode.builder().id(102L).nodeId("NODE-02").build();

        when(edgeNodeRepository.findAll()).thenReturn(List.of(node1, node2));

        EmergencyStopRequest request = EmergencyStopRequest.builder()
                .operatorId("ADMIN-01")
                .fieldId(null)
                .reason("System-wide severe weather alarm")
                .scope("SYSTEM_WIDE")
                .build();

        CommandStatusResponse response = emergencyStopService.executeEmergencyStop(request);

        assertNotNull(response);
        assertNull(response.getFieldId());
        assertEquals(CommandLifecycleState.QUEUED, response.getStatus());
        assertEquals("EMERGENCY_STOP", response.getCommandType());

        verify(auditLogRepository, times(1)).save(any(IrrigationAuditLog.class));
        verify(downlinkQueueService, times(2)).queueDownlink(any(EdgeNode.class), anyString(), eq(response.getCorrelationId()));
        verify(systemEventPublisher, times(1)).publishEmergencyStop(eq(response.getCorrelationId()), isNull(), eq("System-wide severe weather alarm"));
    }

    @Test
    void shouldThrowValidationExceptionWhenReasonIsEmpty() {
        EmergencyStopRequest request = EmergencyStopRequest.builder()
                .operatorId("ADMIN-01")
                .fieldId(1L)
                .reason("   ")
                .build();

        assertThrows(ValidationException.class, () -> emergencyStopService.executeEmergencyStop(request));
    }

    @Test
    void shouldThrowNotFoundWhenFieldDoesNotExist() {
        when(fieldRepository.findById(99L)).thenReturn(Optional.empty());

        EmergencyStopRequest request = EmergencyStopRequest.builder()
                .operatorId("ADMIN-01")
                .fieldId(99L)
                .reason("Test emergency")
                .build();

        assertThrows(ResourceNotFoundException.class, () -> emergencyStopService.executeEmergencyStop(request));
    }
}
