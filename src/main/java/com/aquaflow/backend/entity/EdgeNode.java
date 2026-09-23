package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "edge_nodes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EdgeNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_id", nullable = false, unique = true)
    private String nodeId;

    @Embedded
    private CommunicationIdentity identity;

    @Enumerated(EnumType.STRING)
    @Column(name = "lifecycle_state", nullable = false)
    private NodeLifecycleState lifecycleState;

    @Enumerated(EnumType.STRING)
    @Column(name = "health_state", nullable = false)
    private HealthState healthState;

    @Embedded
    private NodeHealthMetrics healthMetrics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitoring_zone_id")
    private MonitoringZone monitoringZone;

    @Column(name = "hardware_model")
    private String hardwareModel;

    @Column(name = "firmware_version")
    private String firmwareVersion;

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

