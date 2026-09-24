package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "irrigation_command_records")
public class IrrigationCommandRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "correlation_id", unique = true, nullable = false)
    private String correlationId;

    @Column(name = "command_type", nullable = false)
    private String commandType;

    @Column(name = "target_field_id")
    private Long targetFieldId;

    @Column(name = "node_id")
    private Long nodeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_state", nullable = false)
    private CommandState currentState;

    @Column(name = "actor_source")
    private String actorSource;

    @Column(name = "rationale", length = 1000)
    private String rationale;

    @Column(name = "failure_reason", length = 1000)
    private String failureReason;

    @Column(name = "edge_ack_metrics", length = 1000)
    private String edgeAckMetrics;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public IrrigationCommandRecord() {
    }

    public IrrigationCommandRecord(String correlationId, String commandType, Long targetFieldId, Long nodeId,
                                  CommandState currentState, String actorSource, String rationale) {
        this.correlationId = correlationId;
        this.commandType = commandType;
        this.targetFieldId = targetFieldId;
        this.nodeId = nodeId;
        this.currentState = currentState;
        this.actorSource = actorSource;
        this.rationale = rationale;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public Long getTargetFieldId() {
        return targetFieldId;
    }

    public void setTargetFieldId(Long targetFieldId) {
        this.targetFieldId = targetFieldId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public CommandState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(CommandState currentState) {
        this.currentState = currentState;
    }

    public String getActorSource() {
        return actorSource;
    }

    public void setActorSource(String actorSource) {
        this.actorSource = actorSource;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getEdgeAckMetrics() {
        return edgeAckMetrics;
    }

    public void setEdgeAckMetrics(String edgeAckMetrics) {
        this.edgeAckMetrics = edgeAckMetrics;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

