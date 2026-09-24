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
    private Double snr;
    private Integer consecutiveFailures;
    private Boolean isTelemetryStale;
    private LocalDateTime lastTelemetryAt;
    private LocalDateTime lastHeartbeat;
}
