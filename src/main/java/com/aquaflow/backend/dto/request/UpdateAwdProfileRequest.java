package com.aquaflow.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAwdProfileRequest {

    @NotNull(message = "Target moisture percentage is required")
    @Positive(message = "Target moisture percentage must be positive")
    private Double targetMoisturePercentage;

    @Positive(message = "Target drying depth must be positive")
    private Double targetDryingDepthCm;

    @Positive(message = "Flood refill depth must be positive")
    private Double floodRefillDepthCm;

    private Boolean safetyRainOverride;

    public Double getTargetMoisturePercentage() { return targetMoisturePercentage; }
    public void setTargetMoisturePercentage(Double targetMoisturePercentage) { this.targetMoisturePercentage = targetMoisturePercentage; }
    public Double getTargetDryingDepthCm() { return targetDryingDepthCm; }
    public void setTargetDryingDepthCm(Double targetDryingDepthCm) { this.targetDryingDepthCm = targetDryingDepthCm; }
    public Double getFloodRefillDepthCm() { return floodRefillDepthCm; }
    public void setFloodRefillDepthCm(Double floodRefillDepthCm) { this.floodRefillDepthCm = floodRefillDepthCm; }
    public Boolean getSafetyRainOverride() { return safetyRainOverride; }
    public void setSafetyRainOverride(Boolean safetyRainOverride) { this.safetyRainOverride = safetyRainOverride; }
}

