package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.IrrigationAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IrrigationAuditLogRepository extends JpaRepository<IrrigationAuditLog, Long> {
    List<IrrigationAuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);
    Page<IrrigationAuditLog> findByEntityTypeAndEntityId(String entityType, String entityId, Pageable pageable);
    Page<IrrigationAuditLog> findByEventType(String eventType, Pageable pageable);
    Page<IrrigationAuditLog> findByActor(String actor, Pageable pageable);
    Page<IrrigationAuditLog> findByCorrelationId(String correlationId, Pageable pageable);
    List<IrrigationAuditLog> findByCorrelationId(String correlationId);
    Page<IrrigationAuditLog> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
    List<IrrigationAuditLog> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}

