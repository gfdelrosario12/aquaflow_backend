package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.DecisionType;
import com.aquaflow.backend.entity.TriggerReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IrrigationDecisionResponse {
    private Long id;
    private Long edgeNodeId;
    private DecisionType decisionType;
    private TriggerReason triggerReason;
    private Integer requestedDurationMinutes;
    private Double requestedVolumeLiters;
    private String executionStatus;
    private LocalDateTime nodeTimestamp;
    private LocalDateTime createdAt;
}

