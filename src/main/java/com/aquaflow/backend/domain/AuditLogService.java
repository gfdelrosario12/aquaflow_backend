package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.AuditQueryRequest;
import com.aquaflow.backend.dto.response.AuditExportResponse;
import com.aquaflow.backend.dto.response.IrrigationAuditLogResponse;
import com.aquaflow.backend.dto.response.SystemAuditLogResponse;
import com.aquaflow.backend.entity.IrrigationAuditLog;
import com.aquaflow.backend.entity.SystemAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    SystemAuditLog logSystemEvent(String eventType, String actor, String entityType, String entityId, String correlationId, String previousState, String resultingState, Object payload);
    IrrigationAuditLog logIrrigationEvent(String eventType, String actor, String entityType, String entityId, String correlationId, Object payload);
    Page<IrrigationAuditLogResponse> getIrrigationAuditLogs(AuditQueryRequest queryRequest, Pageable pageable);
    Page<SystemAuditLogResponse> getSystemAuditLogs(AuditQueryRequest queryRequest, Pageable pageable);
    AuditExportResponse exportAuditLogs(AuditQueryRequest queryRequest);
}

