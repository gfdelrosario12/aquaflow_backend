package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.CropGrowthStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwdThresholdConfigResponse {
    private Long id;
    private CropGrowthStage growthStage;
    private Double triggerMoisturePercentage;
    private Double targetMoisturePercentage;
    private Double targetFloodDepthCm;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

