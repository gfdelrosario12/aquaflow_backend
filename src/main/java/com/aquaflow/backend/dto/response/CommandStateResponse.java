package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.CommandState;
import java.time.LocalDateTime;

public class CommandStateResponse {

    private Long id;
    private String correlationId;
    private String commandType;
    private Long targetFieldId;
    private Long nodeId;
    private CommandState currentState;
    private String actorSource;
    private String rationale;
    private String failureReason;
    private String edgeAckMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommandStateResponse() {
    }

    public CommandStateResponse(Long id, String correlationId, String commandType, Long targetFieldId, Long nodeId,
                                CommandState currentState, String actorSource, String rationale, String failureReason,
                                String edgeAckMetrics, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.correlationId = correlationId;
        this.commandType = commandType;
        this.targetFieldId = targetFieldId;
        this.nodeId = nodeId;
        this.currentState = currentState;
        this.actorSource = actorSource;
        this.rationale = rationale;
        this.failureReason = failureReason;
        this.edgeAckMetrics = edgeAckMetrics;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

