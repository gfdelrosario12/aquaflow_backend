package com.aquaflow.backend.api;

import com.aquaflow.backend.domain.AuditLogService;
import com.aquaflow.backend.dto.request.AuditQueryRequest;
import com.aquaflow.backend.dto.response.AuditExportResponse;
import com.aquaflow.backend.dto.response.IrrigationAuditLogResponse;
import com.aquaflow.backend.dto.response.SystemAuditLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/irrigation")
    public ResponseEntity<Page<IrrigationAuditLogResponse>> getIrrigationAuditLogs(
            AuditQueryRequest queryRequest,
            Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getIrrigationAuditLogs(queryRequest, pageable));
    }

    @GetMapping("/system")
    public ResponseEntity<Page<SystemAuditLogResponse>> getSystemAuditLogs(
            AuditQueryRequest queryRequest,
            Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getSystemAuditLogs(queryRequest, pageable));
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportAuditLogs(AuditQueryRequest queryRequest) {
        AuditExportResponse exportResponse = auditLogService.exportAuditLogs(queryRequest);

        if ("csv".equalsIgnoreCase(exportResponse.getFormat())) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"audit-export.csv\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(exportResponse.getContent());
        }

        return ResponseEntity.ok(exportResponse);
    }
}

