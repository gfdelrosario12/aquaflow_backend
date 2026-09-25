package com.aquaflow.backend.controller;

import com.aquaflow.backend.api.AuditLogController;
import com.aquaflow.backend.domain.AuditLogService;
import com.aquaflow.backend.dto.request.AuditQueryRequest;
import com.aquaflow.backend.dto.response.AuditExportResponse;
import com.aquaflow.backend.dto.response.IrrigationAuditLogResponse;
import com.aquaflow.backend.dto.response.SystemAuditLogResponse;
import com.aquaflow.backend.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuditLogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuditLogController auditLogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(auditLogController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldGetIrrigationAuditLogsPaged() throws Exception {
        IrrigationAuditLogResponse response = IrrigationAuditLogResponse.builder()
                .id(1L)
                .eventType("MANUAL_IRRIGATION_START")
                .actor("OP-01")
                .entityType("FIELD")
                .entityId("10")
                .createdAt(LocalDateTime.now())
                .build();

        when(auditLogService.getIrrigationAuditLogs(any(AuditQueryRequest.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/audit/irrigation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].eventType").value("MANUAL_IRRIGATION_START"));
    }

    @Test
    void shouldGetSystemAuditLogsPaged() throws Exception {
        SystemAuditLogResponse response = SystemAuditLogResponse.builder()
                .id(2L)
                .eventType("NODE_STATUS_CHANGED")
                .actor("SYSTEM")
                .entityType("NODE")
                .entityId("NODE-01")
                .createdAt(LocalDateTime.now())
                .build();

        when(auditLogService.getSystemAuditLogs(any(AuditQueryRequest.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/audit/system"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(2))
                .andExpect(jsonPath("$.content[0].eventType").value("NODE_STATUS_CHANGED"));
    }

    @Test
    void shouldExportAuditLogsCsv() throws Exception {
        AuditExportResponse response = AuditExportResponse.builder()
                .format("csv")
                .recordCount(2)
                .content("Category,Id\nIRRIGATION,1")
                .exportedAt(LocalDateTime.now())
                .build();

        when(auditLogService.exportAuditLogs(any(AuditQueryRequest.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/audit/export?format=csv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"audit-export.csv\""))
                .andExpect(content().string("Category,Id\nIRRIGATION,1"));
    }

    @Test
    void shouldExportAuditLogsJson() throws Exception {
        AuditExportResponse response = AuditExportResponse.builder()
                .format("json")
                .recordCount(1)
                .content("[{\"id\":1}]")
                .exportedAt(LocalDateTime.now())
                .build();

        when(auditLogService.exportAuditLogs(any(AuditQueryRequest.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/audit/export?format=json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.format").value("json"))
                .andExpect(jsonPath("$.recordCount").value(1));
    }
}

