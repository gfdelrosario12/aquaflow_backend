package com.aquaflow.backend.entity;

public enum NodeLifecycleState {
    UNREGISTERED,
    PROVISIONED,
    COMMISSIONED,
    ACTIVE,
    DEGRADED,
    MAINTENANCE,
    DECOMMISSIONED,
    REPLACED
}
