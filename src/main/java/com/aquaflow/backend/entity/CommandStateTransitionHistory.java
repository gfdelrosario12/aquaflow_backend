package com.aquaflow.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "command_state_transition_histories")
public class CommandStateTransitionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "correlation_id", nullable = false)
    private String correlationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_state")
    private CommandState fromState;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_state", nullable = false)
    private CommandState toState;

    @Column(name = "actor_source")
    private String actorSource;

    @Column(name = "transition_reason", length = 1000)
    private String transitionReason;

    @Column(name = "metadata_json", length = 2000)
    private String metadataJson;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public CommandStateTransitionHistory() {
    }

    public CommandStateTransitionHistory(String correlationId, CommandState fromState, CommandState toState,
                                         String actorSource, String transitionReason, String metadataJson) {
        this.correlationId = correlationId;
        this.fromState = fromState;
        this.toState = toState;
        this.actorSource = actorSource;
        this.transitionReason = transitionReason;
        this.metadataJson = metadataJson;
        this.timestamp = LocalDateTime.now();
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

