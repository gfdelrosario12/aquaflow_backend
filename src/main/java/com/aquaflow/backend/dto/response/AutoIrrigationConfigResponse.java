package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.ScheduleMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoIrrigationConfigResponse {
    private Long id;
    private Long edgeNodeId;
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

