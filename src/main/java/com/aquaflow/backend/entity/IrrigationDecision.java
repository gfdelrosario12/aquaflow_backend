package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "irrigation_decisions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IrrigationDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edge_node_id", nullable = false)
    @ToString.Exclude
    private EdgeNode edgeNode;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision_type", nullable = false)
    private DecisionType decisionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_reason", nullable = false)
    private TriggerReason triggerReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "crop_stage")
    private CropGrowthStage cropStage;

    @Column(name = "confidence")
    private Double confidence;

    @Column(name = "requested_duration_minutes")
    private Integer requestedDurationMinutes;

    @Column(name = "requested_volume_liters")
    private Double requestedVolumeLiters;

    @Column(name = "actual_duration_minutes")
    private Integer actualDurationMinutes;

    @Column(name = "execution_status", nullable = false)
    private String executionStatus;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "config_version")
    private Long configVersion;

    @Column(name = "correlation_id")
    private String correlationId;

    // Telemetry inputs captured at decision time
    @Column(name = "telemetry_water_level_cm")
    private Double telemetryWaterLevelCm;

    @Column(name = "telemetry_soil_moisture_percent")
    private Double telemetrySoilMoisturePercent;

    @Column(name = "telemetry_temperature_c")
    private Double telemetryTemperatureC;

    @Column(name = "battery_percentage")
    private Double batteryPercentage;

    @Column(name = "rssi")
    private Integer rssi;

    @Column(name = "snr")
    private Double snr;

    @Column(name = "node_timestamp", nullable = false)
    private LocalDateTime nodeTimestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (nodeTimestamp == null) {
            nodeTimestamp = LocalDateTime.now();
        }
        if (executionStatus == null) {
            executionStatus = "COMPLETED";
        }
    }
}
