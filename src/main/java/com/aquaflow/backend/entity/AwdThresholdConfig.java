package com.aquaflow.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "awd_threshold_configs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwdThresholdConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_irrigation_config_id")
    @JsonIgnore
    @ToString.Exclude
    private AutoIrrigationConfig autoIrrigationConfig;

    @Enumerated(EnumType.STRING)
    @Column(name = "growth_stage", nullable = false)
    private CropGrowthStage growthStage;

    @Column(name = "trigger_moisture_percentage", nullable = false)
    private Double triggerMoisturePercentage;

    @Column(name = "target_moisture_percentage", nullable = false)
    private Double targetMoisturePercentage;

    @Column(name = "target_flood_depth_cm")
    private Double targetFloodDepthCm;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AutoIrrigationConfig getAutoIrrigationConfig() { return autoIrrigationConfig; }
    public void setAutoIrrigationConfig(AutoIrrigationConfig autoIrrigationConfig) { this.autoIrrigationConfig = autoIrrigationConfig; }
    public CropGrowthStage getGrowthStage() { return growthStage; }
    public void setGrowthStage(CropGrowthStage growthStage) { this.growthStage = growthStage; }
    public Double getTriggerMoisturePercentage() { return triggerMoisturePercentage; }
    public void setTriggerMoisturePercentage(Double triggerMoisturePercentage) { this.triggerMoisturePercentage = triggerMoisturePercentage; }
    public Double getTargetMoisturePercentage() { return targetMoisturePercentage; }
    public void setTargetMoisturePercentage(Double targetMoisturePercentage) { this.targetMoisturePercentage = targetMoisturePercentage; }
    public Double getTargetFloodDepthCm() { return targetFloodDepthCm; }
    public void setTargetFloodDepthCm(Double targetFloodDepthCm) { this.targetFloodDepthCm = targetFloodDepthCm; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

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

