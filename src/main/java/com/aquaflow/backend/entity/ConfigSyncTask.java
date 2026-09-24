package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "config_sync_tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigSyncTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edge_node_id", nullable = false)
    @ToString.Exclude
    private EdgeNode edgeNode;

    @Column(name = "config_version", nullable = false)
    private Long configVersion;

    @Column(name = "correlation_id", nullable = false, unique = true)
    private String correlationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ConfigSyncStatus status = ConfigSyncStatus.PENDING;

    @Column(name = "retry_count", nullable = false)
    @Builder.Default
    private Integer retryCount = 0;

    @Column(name = "max_retries", nullable = false)
    @Builder.Default
    private Integer maxRetries = 3;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = ConfigSyncStatus.PENDING;
        if (retryCount == null) retryCount = 0;
        if (maxRetries == null) maxRetries = 3;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

