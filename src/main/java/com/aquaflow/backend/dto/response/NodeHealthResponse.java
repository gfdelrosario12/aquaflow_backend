package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.HealthState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeHealthResponse {

    private Long id;
    private String nodeId;
    private HealthState healthState;
    private Double batteryLevel;
    private Double solarVoltage;
    private Integer signalDbm;
    private LocalDateTime lastHeartbeat;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public HealthState getHealthState() { return healthState; }
    public void setHealthState(HealthState healthState) { this.healthState = healthState; }
    public Double getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Double batteryLevel) { this.batteryLevel = batteryLevel; }
    public Double getSolarVoltage() { return solarVoltage; }
    public void setSolarVoltage(Double solarVoltage) { this.solarVoltage = solarVoltage; }
    public Integer getSignalDbm() { return signalDbm; }
    public void setSignalDbm(Integer signalDbm) { this.signalDbm = signalDbm; }
    public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
}

