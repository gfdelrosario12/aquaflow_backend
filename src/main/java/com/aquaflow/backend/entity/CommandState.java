package com.aquaflow.backend.entity;

public enum CommandState {
    ACCEPTED,
    QUEUED,
    DOWNLINK_TRANSMITTED,
    EDGE_ACKNOWLEDGED,
    EXECUTING,
    COMPLETED,
    FAILED,
    CANCELLED,
    OVERRIDDEN
}

