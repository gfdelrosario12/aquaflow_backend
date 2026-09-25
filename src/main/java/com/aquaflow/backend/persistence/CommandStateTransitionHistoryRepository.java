package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.CommandStateTransitionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandStateTransitionHistoryRepository extends JpaRepository<CommandStateTransitionHistory, Long> {
    List<CommandStateTransitionHistory> findByCorrelationIdOrderByTimestampAsc(String correlationId);
}

