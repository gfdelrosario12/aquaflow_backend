package com.aquaflow.backend.dto.request;

import com.aquaflow.backend.entity.CommandState;
import jakarta.validation.constraints.NotNull;

public class CommandStateTransitionRequest {

    @NotNull(message = "targetState is required")
    private CommandState targetState;

    private String actorSource;
    private String transitionReason;
    private String metadataJson;
    private String operatorId;
    private String authorizationRole;

    public CommandStateTransitionRequest() {
    }

    public CommandStateTransitionRequest(CommandState targetState, String actorSource, String transitionReason) {
        this.targetState = targetState;
        this.actorSource = actorSource;
        this.transitionReason = transitionReason;
    }

    public CommandState getTargetState() {
        return targetState;
    }

    public void setTargetState(CommandState targetState) {
        this.targetState = targetState;
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

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getAuthorizationRole() {
        return authorizationRole;
    }

    public void setAuthorizationRole(String authorizationRole) {
        this.authorizationRole = authorizationRole;
    }
}

