package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.ScheduleMode;
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
public class AutoIrrigationConfigResponse {
    private Long id;
    private Long fieldId;
    private Long edgeNodeId;
    private Boolean enabled;
    private Integer maxDurationMinutes;
    private Integer minCooldownMinutes;
    private Integer allowedStartHour;
    private Integer allowedEndHour;
    private Double targetFloodDepthCm;
    private Integer rainDelayHours;
    private Double minConfidenceThreshold;
    private Long configVersion;
    private String updatedBy;
    private String changeReason;

    @Builder.Default
    private List<AwdThresholdConfigResponse> thresholds = new ArrayList<>();

    // Legacy fields retained for backwards compatibility
    private ScheduleMode scheduleMode;
    private Double minSoilMoisturePercentage;
    private Double maxSoilMoisturePercentage;
    private Integer maxSingleRunMinutes;
    private Boolean safetyRainOverride;
    private String cronSchedule;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
