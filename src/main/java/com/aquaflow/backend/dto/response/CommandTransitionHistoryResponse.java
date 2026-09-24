package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.CommandState;
import java.time.LocalDateTime;

public class CommandTransitionHistoryResponse {

    private Long id;
    private String correlationId;
    private CommandState fromState;
    private CommandState toState;
    private String actorSource;
    private String transitionReason;
    private String metadataJson;
    private LocalDateTime timestamp;

    public CommandTransitionHistoryResponse() {
    }

    public CommandTransitionHistoryResponse(Long id, String correlationId, CommandState fromState, CommandState toState,
                                            String actorSource, String transitionReason, String metadataJson,
                                            LocalDateTime timestamp) {
        this.id = id;
        this.correlationId = correlationId;
        this.fromState = fromState;
        this.toState = toState;
        this.actorSource = actorSource;
        this.transitionReason = transitionReason;
        this.metadataJson = metadataJson;
        this.timestamp = timestamp;
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

    public CommandState getFromState() {
        return fromState;
    }

    public void setFromState(CommandState fromState) {
        this.fromState = fromState;
    }

    public CommandState getToState() {
        return toState;
    }

    public void setToState(CommandState toState) {
        this.toState = toState;
    }

    public String getActorSource() {
        return actorSource;
    }

    public void setActorSource(String actorSource) {
        this.actorSource = actorSource;
    }

    public String getTransitionReason() {
        return transitionReason;
    }

    public void setTransitionReason(String transitionReason) {
        this.transitionReason = transitionReason;
    }

    public String getMetadataJson() {
        return metadataJson;
    }

    public void setMetadataJson(String metadataJson) {
        this.metadataJson = metadataJson;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

