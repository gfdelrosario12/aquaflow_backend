package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "zone_telemetry_aggregates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneTelemetryAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @Column(name = "field_id", nullable = false)
    private Long fieldId;

    @Column(name = "avg_soil_moisture")
    private Double avgSoilMoisture;

    @Column(name = "avg_temperature")
    private Double avgTemperature;

    @Column(name = "avg_humidity")
    private Double avgHumidity;

    @Column(name = "water_level")
    private Double waterLevel;

    @Column(name = "min_battery_voltage")
    private Double minBatteryVoltage;

    @Column(name = "min_rssi")
    private Integer minRssi;

    @Column(name = "min_snr")
    private Double minSnr;

    @Column(name = "total_nodes")
    private Integer totalNodes;

    @Column(name = "online_nodes")
    private Integer onlineNodes;

    @Column(name = "stale_nodes")
    private Integer staleNodes;

    @Column(name = "offline_nodes")
    private Integer offlineNodes;

    @Enumerated(EnumType.STRING)
    @Column(name = "health_status", nullable = false)
    private ZoneHealthStatus healthStatus;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    @PrePersist
    protected void onCreate() {
        if (calculatedAt == null) {
            calculatedAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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

