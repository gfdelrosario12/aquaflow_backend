package com.aquaflow.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoIrrigationConfigRequest {
    private Boolean enabled;
    private Integer maxDurationMinutes;
    private Integer minCooldownMinutes;
    private Integer allowedStartHour;
    private Integer allowedEndHour;
    private Double targetFloodDepthCm;
    private Integer rainDelayHours;
    private Double minConfidenceThreshold;
    private String updatedBy;
    private String changeReason;

    @Builder.Default
    private List<AwdThresholdConfigRequest> thresholds = new ArrayList<>();
}

