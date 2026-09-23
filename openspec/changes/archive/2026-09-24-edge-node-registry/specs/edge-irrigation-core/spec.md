## ADDED Requirements

### Requirement: System SHALL enforce node lifecycle operational invariants
The system SHALL prohibit `EdgeNode` entities in `UNREGISTERED`, `DECOMMISSIONED`, or `REPLACED` states from ingesting telemetry readings or executing autonomous irrigation schedule synchronization.

#### Scenario: Reject telemetry ingestion from decommissioned node
- **WHEN** a decommissioned or unregistered edge node attempts to submit sensor telemetry or request irrigation schedule sync
- **THEN** system throws a `ValidationException` with error code `NODE_NOT_ACTIVE` and rejects the operation

