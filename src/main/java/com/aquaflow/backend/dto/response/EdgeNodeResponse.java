package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.HealthState;
import com.aquaflow.backend.entity.NodeLifecycleState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EdgeNodeResponse {
    private Long id;
    private String nodeId;
    private String identityType;
    private String identityValue;
    private String macAddress;
    private String serialNumber;
    private NodeLifecycleState lifecycleState;
    private HealthState healthState;
    private Double batteryLevel;
    private Double solarVoltage;
    private Integer signalDbm;
    private LocalDateTime lastHeartbeat;
    private Long monitoringZoneId;
    private String hardwareModel;
    private String firmwareVersion;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

