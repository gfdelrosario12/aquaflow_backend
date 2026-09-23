package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private EdgeNode edgeNode;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision_type", nullable = false)
    private DecisionType decisionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_reason", nullable = false)
    private TriggerReason triggerReason;

    @Column(name = "requested_duration_minutes")
    private Integer requestedDurationMinutes;

    @Column(name = "requested_volume_liters")
    private Double requestedVolumeLiters;

    @Column(name = "execution_status", nullable = false)
    private String executionStatus;

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
    }
}

