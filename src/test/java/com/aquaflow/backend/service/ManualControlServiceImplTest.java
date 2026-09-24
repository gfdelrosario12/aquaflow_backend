package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.DownlinkQueueService;
import com.aquaflow.backend.domain.ManualControlServiceImpl;
import com.aquaflow.backend.dto.request.ManualStartRequest;
import com.aquaflow.backend.dto.request.ManualStopRequest;
import com.aquaflow.backend.dto.response.CommandStatusResponse;
import com.aquaflow.backend.entity.*;
import com.aquaflow.backend.infrastructure.event.SystemEventPublisher;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.aquaflow.backend.persistence.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ManualControlServiceImplTest {

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private EdgeNodeRepository edgeNodeRepository;

    @Mock
    private MonitoringZoneRepository monitoringZoneRepository;

    @Mock
    private DownlinkQueueService downlinkQueueService;

    @Mock
    private DownlinkQueueItemRepository downlinkQueueItemRepository;

    @Mock
    private IrrigationAuditLogRepository auditLogRepository;

    @Mock
    private SystemEventPublisher systemEventPublisher;

    private ObjectMapper objectMapper;

    private ManualControlServiceImpl manualControlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        manualControlService = new ManualControlServiceImpl(
                fieldRepository,
                edgeNodeRepository,
                monitoringZoneRepository,
                downlinkQueueService,
                downlinkQueueItemRepository,
                auditLogRepository,
                systemEventPublisher,
                objectMapper
        );
    }

    @Test
    void shouldStartManualIrrigationSuccessfully() {
        Field field = Field.builder().id(1L).name("Test Field").build();
        MonitoringZone zone = MonitoringZone.builder().id(10L).field(field).build();
        EdgeNode node = EdgeNode.builder().id(100L).nodeId("NODE-01").monitoringZone(zone).build();

        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
        when(monitoringZoneRepository.findByFieldId(1L)).thenReturn(List.of(zone));
        when(edgeNodeRepository.findByMonitoringZoneId(10L)).thenReturn(List.of(node));

        ManualStartRequest request = ManualStartRequest.builder()
                .operatorId("OP-001")
                .fieldId(1L)
                .rationale("Dry soil test")
                .durationMinutes(45)
                .targetWaterDepthCm(5.0)
                .overridePolicy(false)
                .build();

        CommandStatusResponse response = manualControlService.startManualIrrigation(request);

        assertNotNull(response);
        assertNotNull(response.getCorrelationId());
        assertEquals(1L, response.getFieldId());
        assertEquals("OP-001", response.getOperatorId());
        assertEquals(CommandLifecycleState.QUEUED, response.getStatus());
        assertEquals("MANUAL_START", response.getCommandType());

        verify(auditLogRepository, times(1)).save(any(IrrigationAuditLog.class));
        verify(downlinkQueueService, times(1)).queueDownlink(eq(node), anyString(), eq(response.getCorrelationId()));
    }

    @Test
    void shouldThrowExceptionWhenFieldNotFoundOnStart() {
        when(fieldRepository.findById(99L)).thenReturn(Optional.empty());

        ManualStartRequest request = ManualStartRequest.builder()
                .operatorId("OP-001")
                .fieldId(99L)
                .rationale("Test")
                .durationMinutes(30)
                .build();

        assertThrows(ResourceNotFoundException.class, () -> manualControlService.startManualIrrigation(request));
    }

    @Test
    void shouldThrowValidationExceptionForInvalidDuration() {
        Field field = Field.builder().id(1L).name("Test Field").build();
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));

        ManualStartRequest request = ManualStartRequest.builder()
                .operatorId("OP-001")
                .fieldId(1L)
                .rationale("Test")
                .durationMinutes(2000) // Exceeds 1440 limit
                .build();

        assertThrows(ValidationException.class, () -> manualControlService.startManualIrrigation(request));
    }

    @Test
    void shouldStopManualIrrigationSuccessfully() {
        Field field = Field.builder().id(1L).name("Test Field").build();
        MonitoringZone zone = MonitoringZone.builder().id(10L).field(field).build();
        EdgeNode node = EdgeNode.builder().id(100L).nodeId("NODE-01").monitoringZone(zone).build();

        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
        when(monitoringZoneRepository.findByFieldId(1L)).thenReturn(List.of(zone));
        when(edgeNodeRepository.findByMonitoringZoneId(10L)).thenReturn(List.of(node));

        ManualStopRequest request = ManualStopRequest.builder()
                .operatorId("OP-001")
                .fieldId(1L)
                .rationale("Emergency condition clear")
                .build();

        CommandStatusResponse response = manualControlService.stopManualIrrigation(request);

        assertNotNull(response);
        assertEquals(CommandLifecycleState.QUEUED, response.getStatus());
        assertEquals("MANUAL_STOP", response.getCommandType());
        verify(auditLogRepository, times(1)).save(any(IrrigationAuditLog.class));
        verify(downlinkQueueService, times(1)).queueDownlink(eq(node), anyString(), eq(response.getCorrelationId()));
    }

    @Test
    void shouldGetCommandStatusFromCache() {
        Field field = Field.builder().id(1L).name("Test Field").build();
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));

        ManualStartRequest request = ManualStartRequest.builder()
                .operatorId("OP-001")
                .fieldId(1L)
                .rationale("Test")
                .durationMinutes(30)
                .build();

        CommandStatusResponse startResponse = manualControlService.startManualIrrigation(request);

        CommandStatusResponse fetchedResponse = manualControlService.getCommandStatus(startResponse.getCorrelationId());
        assertNotNull(fetchedResponse);
        assertEquals(startResponse.getCorrelationId(), fetchedResponse.getCorrelationId());
        assertEquals(CommandLifecycleState.QUEUED, fetchedResponse.getStatus());
    }

    @Test
    void shouldThrowNotFoundForUnknownCorrelationId() {
        when(downlinkQueueItemRepository.findByCorrelationId("invalid-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> manualControlService.getCommandStatus("invalid-id"));
    }
}

