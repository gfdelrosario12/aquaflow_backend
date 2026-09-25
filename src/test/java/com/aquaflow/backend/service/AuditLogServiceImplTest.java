package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.AuditLogServiceImpl;
import com.aquaflow.backend.dto.request.AuditQueryRequest;
import com.aquaflow.backend.dto.response.AuditExportResponse;
import com.aquaflow.backend.dto.response.IrrigationAuditLogResponse;
import com.aquaflow.backend.dto.response.SystemAuditLogResponse;
import com.aquaflow.backend.entity.IrrigationAuditLog;
import com.aquaflow.backend.entity.SystemAuditLog;
import com.aquaflow.backend.persistence.IrrigationAuditLogRepository;
import com.aquaflow.backend.persistence.SystemAuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuditLogServiceImplTest {

    @Mock
    private SystemAuditLogRepository systemAuditLogRepository;

    @Mock
    private IrrigationAuditLogRepository irrigationAuditLogRepository;

    private ObjectMapper objectMapper;

    private AuditLogServiceImpl auditLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        auditLogService = new AuditLogServiceImpl(systemAuditLogRepository, irrigationAuditLogRepository, objectMapper);
    }

    @Test
    void shouldLogSystemEventSuccessfully() {
        SystemAuditLog savedLog = SystemAuditLog.builder()
                .id(1L)
                .eventType("NODE_STATUS_CHANGED")
                .actor("SYSTEM")
                .entityType("NODE")
                .entityId("NODE-01")
                .correlationId("corr-123")
                .previousState("OFFLINE")
                .resultingState("ONLINE")
                .createdAt(LocalDateTime.now())
                .build();

        when(systemAuditLogRepository.save(any(SystemAuditLog.class))).thenReturn(savedLog);

        SystemAuditLog result = auditLogService.logSystemEvent("NODE_STATUS_CHANGED", "SYSTEM", "NODE", "NODE-01", "corr-123", "OFFLINE", "ONLINE", "payload");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("NODE_STATUS_CHANGED", result.getEventType());
        verify(systemAuditLogRepository, times(1)).save(any(SystemAuditLog.class));
    }

    @Test
    void shouldLogIrrigationEventSuccessfully() {
        IrrigationAuditLog savedLog = IrrigationAuditLog.builder()
                .id(10L)
                .eventType("MANUAL_IRRIGATION_START")
                .actor("OP-01")
                .entityType("FIELD")
                .entityId("1")
                .correlationId("corr-456")
                .createdAt(LocalDateTime.now())
                .build();

        when(irrigationAuditLogRepository.save(any(IrrigationAuditLog.class))).thenReturn(savedLog);

        IrrigationAuditLog result = auditLogService.logIrrigationEvent("MANUAL_IRRIGATION_START", "OP-01", "FIELD", "1", "corr-456", "payload");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("MANUAL_IRRIGATION_START", result.getEventType());
        verify(irrigationAuditLogRepository, times(1)).save(any(IrrigationAuditLog.class));
    }

    @Test
    void shouldGetIrrigationAuditLogsPaged() {
        IrrigationAuditLog logItem = IrrigationAuditLog.builder()
                .id(10L)
                .eventType("MANUAL_IRRIGATION_START")
                .actor("OP-01")
                .entityType("FIELD")
                .entityId("1")
                .correlationId("corr-456")
                .createdAt(LocalDateTime.now())
                .build();

        when(irrigationAuditLogRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(logItem), PageRequest.of(0, 10), 1));

        Page<IrrigationAuditLogResponse> page = auditLogService.getIrrigationAuditLogs(null, PageRequest.of(0, 10));

        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals("MANUAL_IRRIGATION_START", page.getContent().get(0).getEventType());
    }

    @Test
    void shouldGetSystemAuditLogsPaged() {
        SystemAuditLog logItem = SystemAuditLog.builder()
                .id(1L)
                .eventType("NODE_STATUS_CHANGED")
                .actor("SYSTEM")
                .entityType("NODE")
                .entityId("NODE-01")
                .createdAt(LocalDateTime.now())
                .build();

        when(systemAuditLogRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(logItem), PageRequest.of(0, 10), 1));

        Page<SystemAuditLogResponse> page = auditLogService.getSystemAuditLogs(null, PageRequest.of(0, 10));

        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals("NODE_STATUS_CHANGED", page.getContent().get(0).getEventType());
    }

    @Test
    void shouldExportAuditLogsAsCsv() {
        IrrigationAuditLog irLog = IrrigationAuditLog.builder()
                .id(10L).eventType("MANUAL_IRRIGATION_START").actor("OP-01").entityType("FIELD").entityId("1").correlationId("corr-1").createdAt(LocalDateTime.now()).build();
        SystemAuditLog sysLog = SystemAuditLog.builder()
                .id(1L).eventType("NODE_STATUS_CHANGED").actor("SYSTEM").entityType("NODE").entityId("N1").correlationId("corr-2").previousState("OFF").resultingState("ON").createdAt(LocalDateTime.now()).build();

        when(irrigationAuditLogRepository.findAll()).thenReturn(List.of(irLog));
        when(systemAuditLogRepository.findAll()).thenReturn(List.of(sysLog));

        AuditQueryRequest query = AuditQueryRequest.builder().format("csv").build();
        AuditExportResponse response = auditLogService.exportAuditLogs(query);

        assertNotNull(response);
        assertEquals("csv", response.getFormat());
        assertEquals(2, response.getRecordCount());
        assertTrue(response.getContent().contains("Category,Id,EventType"));
        assertTrue(response.getContent().contains("IRRIGATION"));
        assertTrue(response.getContent().contains("SYSTEM"));
    }

    @Test
    void shouldExportAuditLogsAsJson() {
        IrrigationAuditLog irLog = IrrigationAuditLog.builder()
                .id(10L).eventType("MANUAL_IRRIGATION_START").actor("OP-01").entityType("FIELD").entityId("1").correlationId("corr-1").createdAt(LocalDateTime.now()).build();

        when(irrigationAuditLogRepository.findAll()).thenReturn(List.of(irLog));
        when(systemAuditLogRepository.findAll()).thenReturn(List.of());

        AuditQueryRequest query = AuditQueryRequest.builder().format("json").build();
        AuditExportResponse response = auditLogService.exportAuditLogs(query);

        assertNotNull(response);
        assertEquals("json", response.getFormat());
        assertEquals(1, response.getRecordCount());
        assertTrue(response.getContent().contains("MANUAL_IRRIGATION_START"));
    }
}
