package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edge_node_id", unique = true)
    private EdgeNode edgeNode;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_mode", nullable = false)
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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

