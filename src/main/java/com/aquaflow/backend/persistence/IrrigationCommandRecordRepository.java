package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.CommandState;
import com.aquaflow.backend.entity.IrrigationCommandRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IrrigationCommandRecordRepository extends JpaRepository<IrrigationCommandRecord, Long> {
    Optional<IrrigationCommandRecord> findByCorrelationId(String correlationId);
    List<IrrigationCommandRecord> findByTargetFieldIdOrderByCreatedAtDesc(Long targetFieldId);
    List<IrrigationCommandRecord> findByCurrentState(CommandState currentState);
}

