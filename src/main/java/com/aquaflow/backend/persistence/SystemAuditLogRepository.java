package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.SystemAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemAuditLogRepository extends JpaRepository<SystemAuditLog, Long> {
    Page<SystemAuditLog> findByEventType(String eventType, Pageable pageable);
    Page<SystemAuditLog> findByActor(String actor, Pageable pageable);
    Page<SystemAuditLog> findByEntityTypeAndEntityId(String entityType, String entityId, Pageable pageable);
    Page<SystemAuditLog> findByCorrelationId(String correlationId, Pageable pageable);
    List<SystemAuditLog> findByCorrelationId(String correlationId);
    Page<SystemAuditLog> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
    List<SystemAuditLog> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}

