package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.SensorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringPointResponse {
    private Long id;
    private String name;
    private Long monitoringZoneId;
    private Long edgeNodeId;
    private SensorType primarySensorType;
    private Double depthCm;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getMonitoringZoneId() { return monitoringZoneId; }
    public void setMonitoringZoneId(Long monitoringZoneId) { this.monitoringZoneId = monitoringZoneId; }
    public Long getEdgeNodeId() { return edgeNodeId; }
    public void setEdgeNodeId(Long edgeNodeId) { this.edgeNodeId = edgeNodeId; }
    public SensorType getPrimarySensorType() { return primarySensorType; }
    public void setPrimarySensorType(SensorType primarySensorType) { this.primarySensorType = primarySensorType; }
    public Double getDepthCm() { return depthCm; }
    public void setDepthCm(Double depthCm) { this.depthCm = depthCm; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

