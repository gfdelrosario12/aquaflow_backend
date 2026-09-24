## ADDED Requirements

### Requirement: System SHALL expose endpoints for reporting and querying autonomous irrigation decisions
The system SHALL expose REST endpoints under `/api/v1/irrigation/` to ingest edge-driven AWD decisions, query decision history, and inspect active field irrigation status.

#### Scenario: Post new edge irrigation decision
- **WHEN** client posts decision payload to `/api/v1/irrigation/decisions`
- **THEN** system persists decision metadata and returns Created 201 response

#### Scenario: Retrieve field active irrigation status
- **WHEN** client requests GET `/api/v1/irrigation/field/{fieldId}/status`
- **THEN** system returns active irrigation state and summary for the target field

