## ADDED Requirements

### Requirement: System SHALL persist autonomous edge-originated decisions
The system SHALL record decisions generated locally by an `EdgeNode` in an `IrrigationDecision` entity, capturing decision type, triggered threshold rule, requested volume/duration, node timestamp, and execution status.

#### Scenario: Record edge node decision
- **WHEN** edge node transmits an autonomous decision log to backend
- **THEN** system persists the `IrrigationDecision` record referencing the node and decision parameters without altering the edge decision rationale

### Requirement: System SHALL maintain distributed node configuration persistence
The system SHALL persist `AutoIrrigationConfig` containing threshold settings, schedules, schedule modes, and safety override limits distributed to specific `EdgeNode` or `MonitoringZone` instances with optimistic locking.

#### Scenario: Update edge node configuration
- **WHEN** cloud operator modifies thresholds or schedule mode for a node
- **THEN** system updates `AutoIrrigationConfig`, increments `@Version`, and flags configuration for edge node sync

