package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auto_irrigation_configs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoIrrigationConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    @ToString.Exclude
    private Field field;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edge_node_id", unique = true)
    @ToString.Exclude
    private EdgeNode edgeNode;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Column(name = "max_duration_minutes")
    private Integer maxDurationMinutes;

    @Column(name = "min_cooldown_minutes")
    private Integer minCooldownMinutes;

    @Column(name = "allowed_start_hour")
    private Integer allowedStartHour;

    @Column(name = "allowed_end_hour")
    private Integer allowedEndHour;

    @Column(name = "target_flood_depth_cm")
    private Double targetFloodDepthCm;

    @Column(name = "rain_delay_hours")
    private Integer rainDelayHours;

    @Column(name = "min_confidence_threshold")
    private Double minConfidenceThreshold;

    @Column(name = "config_version", nullable = false)
    @Builder.Default
    private Long configVersion = 1L;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "change_reason")
    private String changeReason;

    @OneToMany(mappedBy = "autoIrrigationConfig", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<AwdThresholdConfig> thresholds = new ArrayList<>();

    // Legacy fields retained for backwards compatibility
    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_mode")
    private ScheduleMode scheduleMode;

    @Column(name = "min_soil_moisture_percentage")
    private Double minSoilMoisturePercentage;

    @Column(name = "max_soil_moisture_percentage")
    private Double maxSoilMoisturePercentage;

    @Column(name = "max_single_run_minutes")
    private Integer maxSingleRunMinutes;

    @Column(name = "safety_rain_override")
    private Boolean safetyRainOverride;

    @Column(name = "cron_schedule")
    private String cronSchedule;

    @Version
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (configVersion == null) {
            configVersion = 1L;
        }
        if (enabled == null) {
            enabled = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
