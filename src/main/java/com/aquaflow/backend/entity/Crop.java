package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "crops")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "water_per_stage")
    private Double waterPerStage;

    @Column(name = "growing_season_days")
    private Integer growingSeasonDays;

    @Column(name = "optimal_temperature_min")
    private Double optimalTemperatureMin;

    @Column(name = "optimal_temperature_max")
    private Double optimalTemperatureMax;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getWaterPerStage() { return waterPerStage; }
    public void setWaterPerStage(Double waterPerStage) { this.waterPerStage = waterPerStage; }
    public Integer getGrowingSeasonDays() { return growingSeasonDays; }
    public void setGrowingSeasonDays(Integer growingSeasonDays) { this.growingSeasonDays = growingSeasonDays; }
    public Double getOptimalTemperatureMin() { return optimalTemperatureMin; }
    public void setOptimalTemperatureMin(Double optimalTemperatureMin) { this.optimalTemperatureMin = optimalTemperatureMin; }
    public Double getOptimalTemperatureMax() { return optimalTemperatureMax; }
    public void setOptimalTemperatureMax(Double optimalTemperatureMax) { this.optimalTemperatureMax = optimalTemperatureMax; }
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