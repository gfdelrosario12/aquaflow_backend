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
public class ZoneTelemetryResponse {

    private Long zoneId;
    private Long fieldId;
    private Double avgSoilMoisture;
    private Double avgTemperature;
    private Double avgHumidity;
    private Double waterLevel;
    private Double minBatteryVoltage;
    private Integer minRssi;
    private Double minSnr;
    private Integer totalNodes;
    private Integer onlineNodes;
    private Integer staleNodes;
    private Integer offlineNodes;
    private ZoneHealthStatus healthStatus;
    private LocalDateTime calculatedAt;

    public Long getZoneId() { return zoneId; }
    public void setZoneId(Long zoneId) { this.zoneId = zoneId; }
    public Long getFieldId() { return fieldId; }
    public void setFieldId(Long fieldId) { this.fieldId = fieldId; }
    public Double getAvgSoilMoisture() { return avgSoilMoisture; }
    public void setAvgSoilMoisture(Double avgSoilMoisture) { this.avgSoilMoisture = avgSoilMoisture; }
    public Double getAvgTemperature() { return avgTemperature; }
    public void setAvgTemperature(Double avgTemperature) { this.avgTemperature = avgTemperature; }
    public Double getAvgHumidity() { return avgHumidity; }
    public void setAvgHumidity(Double avgHumidity) { this.avgHumidity = avgHumidity; }
    public Double getWaterLevel() { return waterLevel; }
    public void setWaterLevel(Double waterLevel) { this.waterLevel = waterLevel; }
    public Double getMinBatteryVoltage() { return minBatteryVoltage; }
    public void setMinBatteryVoltage(Double minBatteryVoltage) { this.minBatteryVoltage = minBatteryVoltage; }
    public Integer getMinRssi() { return minRssi; }
    public void setMinRssi(Integer minRssi) { this.minRssi = minRssi; }
    public Double getMinSnr() { return minSnr; }
    public void setMinSnr(Double minSnr) { this.minSnr = minSnr; }
    public Integer getTotalNodes() { return totalNodes; }
    public void setTotalNodes(Integer totalNodes) { this.totalNodes = totalNodes; }
    public Integer getOnlineNodes() { return onlineNodes; }
    public void setOnlineNodes(Integer onlineNodes) { this.onlineNodes = onlineNodes; }
    public Integer getStaleNodes() { return staleNodes; }
    public void setStaleNodes(Integer staleNodes) { this.staleNodes = staleNodes; }
    public Integer getOfflineNodes() { return offlineNodes; }
    public void setOfflineNodes(Integer offlineNodes) { this.offlineNodes = offlineNodes; }
    public ZoneHealthStatus getHealthStatus() { return healthStatus; }
    public void setHealthStatus(ZoneHealthStatus healthStatus) { this.healthStatus = healthStatus; }
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
}

