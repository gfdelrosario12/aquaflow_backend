package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "monitoring_points")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitoring_zone_id", nullable = false)
    private MonitoringZone monitoringZone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edge_node_id")
    private EdgeNode edgeNode;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_sensor_type", nullable = false)
    private SensorType primarySensorType;

    @Column(name = "depth_cm")
    private Double depthCm;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

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

