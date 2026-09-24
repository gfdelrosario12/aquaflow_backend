package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.DecisionType;
import com.aquaflow.backend.entity.IrrigationDecision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IrrigationDecisionRepository extends JpaRepository<IrrigationDecision, Long> {
    List<IrrigationDecision> findByEdgeNodeId(Long edgeNodeId);
    Page<IrrigationDecision> findByEdgeNodeId(Long edgeNodeId, Pageable pageable);
    Page<IrrigationDecision> findByEdgeNodeIdAndNodeTimestampBetween(Long edgeNodeId, LocalDateTime from, LocalDateTime to, Pageable pageable);
    List<IrrigationDecision> findByDecisionType(DecisionType decisionType);
    Page<IrrigationDecision> findByExecutionStatus(String executionStatus, Pageable pageable);
    List<IrrigationDecision> findByEdgeNodeIdIn(List<Long> edgeNodeIds);
    List<IrrigationDecision> findTop10ByEdgeNodeIdInOrderByNodeTimestampDesc(List<Long> edgeNodeIds);
    List<IrrigationDecision> findByEdgeNodeIdInAndNodeTimestampAfter(List<Long> edgeNodeIds, LocalDateTime after);
}
