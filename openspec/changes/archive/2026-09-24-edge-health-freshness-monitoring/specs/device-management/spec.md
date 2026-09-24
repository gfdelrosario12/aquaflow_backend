## ADDED Requirements

### Requirement: System SHALL expose REST endpoints for edge node health monitoring and freshness inspection
The system SHALL expose REST endpoints under `/api/v1/devices/nodes/health` to retrieve node health summaries, telemetry freshness indicators, and health metrics history.

#### Scenario: Retrieve edge node health summary
- **WHEN** client sends GET `/api/v1/devices/nodes/health`
- **THEN** system returns health summaries for all registered edge nodes including status, battery, RSSI, and last seen timestamp

#### Scenario: Retrieve specific edge node health detail
- **WHEN** client sends GET `/api/v1/devices/nodes/{id}/health`
- **THEN** system returns detailed health metrics, freshness status, and communication failure history for the target node

