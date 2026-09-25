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

    public CropGrowthStage getGrowthStage() { return growthStage; }
    public void setGrowthStage(CropGrowthStage growthStage) { this.growthStage = growthStage; }
    public Double getTriggerMoisturePercentage() { return triggerMoisturePercentage; }
    public void setTriggerMoisturePercentage(Double triggerMoisturePercentage) { this.triggerMoisturePercentage = triggerMoisturePercentage; }
    public Double getTargetMoisturePercentage() { return targetMoisturePercentage; }
    public void setTargetMoisturePercentage(Double targetMoisturePercentage) { this.targetMoisturePercentage = targetMoisturePercentage; }
    public Double getTargetFloodDepthCm() { return targetFloodDepthCm; }
    public void setTargetFloodDepthCm(Double targetFloodDepthCm) { this.targetFloodDepthCm = targetFloodDepthCm; }
}

