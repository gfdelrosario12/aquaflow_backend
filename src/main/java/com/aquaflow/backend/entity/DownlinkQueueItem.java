package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "downlink_queue_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownlinkQueueItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edge_node_id", nullable = false)
    @ToString.Exclude
    private EdgeNode edgeNode;

    @Column(name = "correlation_id", nullable = false)
    private String correlationId;

    @Column(name = "payload_json", columnDefinition = "TEXT", nullable = false)
    private String payloadJson;

    @Column(name = "f_port", nullable = false)
    @Builder.Default
    private Integer fPort = 10;

    @Column(name = "confirmed", nullable = false)
    @Builder.Default
    private Boolean confirmed = true;

    @Column(name = "retry_count", nullable = false)
    @Builder.Default
    private Integer retryCount = 0;

    @Column(name = "max_retries", nullable = false)
    @Builder.Default
    private Integer maxRetries = 3;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ConfigSyncStatus status = ConfigSyncStatus.QUEUED;

    @Column(name = "last_attempt_at")
    private LocalDateTime lastAttemptAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (fPort == null) fPort = 10;
        if (confirmed == null) confirmed = true;
        if (retryCount == null) retryCount = 0;
        if (maxRetries == null) maxRetries = 3;
        if (status == null) status = ConfigSyncStatus.QUEUED;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

