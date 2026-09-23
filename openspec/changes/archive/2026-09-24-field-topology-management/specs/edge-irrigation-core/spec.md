## ADDED Requirements

### Requirement: System SHALL validate topology assignment constraints
The system SHALL enforce assignment integrity prohibiting `MonitoringPoint` entities from referencing unassigned or foreign `EdgeNode` devices, and preventing `EdgeNode` assignment to `MonitoringZone` instances outside its designated `Field`.

#### Scenario: Reject monitoring point creation with invalid edge node
- **WHEN** client creates a `MonitoringPoint` referencing an `EdgeNode` that belongs to a different `MonitoringZone` or `Field`
- **THEN** system throws a `ValidationException` with error code `INVALID_TOPOLOGY_ASSIGNMENT` and rejects the operation

