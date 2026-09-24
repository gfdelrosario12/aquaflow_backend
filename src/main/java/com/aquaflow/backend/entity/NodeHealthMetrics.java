package com.aquaflow.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeHealthMetrics {

    @Column(name = "battery_level")
    private Double batteryLevel;

    @Column(name = "solar_voltage")
    private Double solarVoltage;

    @Column(name = "signal_dbm")
    private Integer signalDbm;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    @Column(name = "snr")
    private Double snr;

    @Column(name = "consecutive_failures")
    private Integer consecutiveFailures;

    @Column(name = "is_telemetry_stale")
    private Boolean isTelemetryStale;

    @Column(name = "last_telemetry_at")
    private LocalDateTime lastTelemetryAt;
}

