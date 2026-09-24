package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.CommandStateTransitionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandStateTransitionHistoryRepository extends JpaRepository<CommandStateTransitionHistory, Long> {
    List<CommandStateTransitionHistory> findByCorrelationIdOrderByTimestampAsc(String correlationId);
}

