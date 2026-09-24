package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.ZoneHealthStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneTelemetryTrendPoint {

    private LocalDateTime timestamp;
    private Double avgSoilMoisture;
    private Double avgTemperature;
    private Double waterLevel;
    private ZoneHealthStatus healthStatus;

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Double getAvgSoilMoisture() { return avgSoilMoisture; }
    public void setAvgSoilMoisture(Double avgSoilMoisture) { this.avgSoilMoisture = avgSoilMoisture; }
    public Double getAvgTemperature() { return avgTemperature; }
    public void setAvgTemperature(Double avgTemperature) { this.avgTemperature = avgTemperature; }
    public Double getWaterLevel() { return waterLevel; }
    public void setWaterLevel(Double waterLevel) { this.waterLevel = waterLevel; }
    public ZoneHealthStatus getHealthStatus() { return healthStatus; }
    public void setHealthStatus(ZoneHealthStatus healthStatus) { this.healthStatus = healthStatus; }
}

