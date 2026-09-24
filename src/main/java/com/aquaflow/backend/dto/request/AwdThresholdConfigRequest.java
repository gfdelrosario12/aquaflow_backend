package com.aquaflow.backend.dto.request;

import com.aquaflow.backend.entity.CropGrowthStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwdThresholdConfigRequest {
    private CropGrowthStage growthStage;
    private Double triggerMoisturePercentage;
    private Double targetMoisturePercentage;
    private Double targetFloodDepthCm;
}

