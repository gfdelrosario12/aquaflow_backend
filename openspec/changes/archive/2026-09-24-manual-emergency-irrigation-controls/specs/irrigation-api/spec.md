## ADDED Requirements

### Requirement: System SHALL expose endpoints for manual irrigation control and emergency stop
The system SHALL expose REST endpoints under `/api/v1/irrigation/` for operator-initiated manual start (`POST /api/v1/irrigation/manual/start`), manual stop (`POST /api/v1/irrigation/manual/stop`), and emergency stop (`POST /api/v1/irrigation/emergency-stop`).

#### Scenario: Post manual irrigation start request
- **WHEN** client posts a valid manual start payload to `/api/v1/irrigation/manual/start`
- **THEN** system returns 202 Accepted or 200 OK with command correlation ID and initial QUEUED status

#### Scenario: Post manual irrigation stop request
- **WHEN** client posts a valid manual stop payload to `/api/v1/irrigation/manual/stop`
- **THEN** system returns 202 Accepted or 200 OK with command correlation ID and queued cancellation status

#### Scenario: Post emergency stop request
- **WHEN** client posts an emergency stop payload to `/api/v1/irrigation/emergency-stop`
- **THEN** system returns 202 Accepted or 200 OK with emergency execution details and highest-priority status

