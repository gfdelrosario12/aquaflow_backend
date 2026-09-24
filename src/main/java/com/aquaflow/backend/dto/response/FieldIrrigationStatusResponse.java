package com.aquaflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldIrrigationStatusResponse {
    private Long fieldId;
    private String fieldName;
    private Boolean isIrrigating;
    private Integer activeIrrigatingNodesCount;
    private Double todayDeliveredVolumeLiters;
    private LocalDateTime lastUpdated;

    @Builder.Default
    private List<IrrigationDecisionResponse> recentDecisions = new ArrayList<>();
}

