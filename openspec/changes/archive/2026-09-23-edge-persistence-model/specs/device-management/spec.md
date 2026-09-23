## ADDED Requirements

### Requirement: System SHALL represent edge node lifecycle and communication identity
The system SHALL represent `EdgeNode` with hardware identity (MAC address, serial number, hardware model), active status (`ONLINE`, `OFFLINE`, `DEGRADED`, `MAINTENANCE`), health metrics (battery level, solar voltage, signal strength), firmware version, and assignment to a `MonitoringZone`.

#### Scenario: Edge node status telemetry update
- **WHEN** edge node reports heartbeat and health status
- **THEN** system updates `EdgeNode` status, health metrics, and last seen timestamp in persistence

### Requirement: System SHALL represent read-oriented monitoring zones
The system SHALL aggregate edge nodes and monitoring points into `MonitoringZone` entities, providing read-oriented summary views of assigned nodes, active points, and aggregate telemetry.

#### Scenario: Query monitoring zone aggregation
- **WHEN** client queries `MonitoringZone` by ID
- **THEN** system returns zone detail aggregating assigned edge nodes and monitoring points

