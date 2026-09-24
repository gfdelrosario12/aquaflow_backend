package com.aquaflow.backend.entity;

public enum CommandLifecycleState {
    QUEUED,
    DELIVERED,
    ACKNOWLEDGED,
    EXECUTED,
    REJECTED_SAFETY_INTERLOCK,
    FAILED
}

