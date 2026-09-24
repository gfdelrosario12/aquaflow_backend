## ADDED Requirements

### Requirement: System SHALL expose REST endpoints for irrigation command state machine lifecycle and transition history
The system SHALL expose REST endpoints under `/api/v1/irrigation/commands/` to query command state machine status (`GET /api/v1/irrigation/commands/{commandId}`), transition history (`GET /api/v1/irrigation/commands/{commandId}/history`), and active field commands (`GET /api/v1/irrigation/commands/field/{fieldId}`).

#### Scenario: Retrieve command lifecycle status and transition history
- **WHEN** client requests GET `/api/v1/irrigation/commands/{commandId}/history`
- **THEN** system returns the full ordered sequence of `CommandStateTransitionHistory` records for the specified command correlation ID

