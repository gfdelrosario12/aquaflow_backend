package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.IrrigationAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IrrigationAuditLogRepository extends JpaRepository<IrrigationAuditLog, Long> {
    List<IrrigationAuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);
    Page<IrrigationAuditLog> findByEntityTypeAndEntityId(String entityType, String entityId, Pageable pageable);
    Page<IrrigationAuditLog> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
}

