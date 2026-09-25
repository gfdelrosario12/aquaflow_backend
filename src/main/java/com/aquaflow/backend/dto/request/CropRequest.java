package com.aquaflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CropRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double waterPerStage;

    private Integer growingSeasonDays;

    private Double optimalTemperatureMin;

    private Double optimalTemperatureMax;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getWaterPerStage() { return waterPerStage; }
    public void setWaterPerStage(Double waterPerStage) { this.waterPerStage = waterPerStage; }
    public Integer getGrowingSeasonDays() { return growingSeasonDays; }
    public void setGrowingSeasonDays(Integer growingSeasonDays) { this.growingSeasonDays = growingSeasonDays; }
    public Double getOptimalTemperatureMin() { return optimalTemperatureMin; }
    public void setOptimalTemperatureMin(Double optimalTemperatureMin) { this.optimalTemperatureMin = optimalTemperatureMin; }
    public Double getOptimalTemperatureMax() { return optimalTemperatureMax; }
    public void setOptimalTemperatureMax(Double optimalTemperatureMax) { this.optimalTemperatureMax = optimalTemperatureMax; }
}