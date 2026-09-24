## Purpose

Manages the reporting, persistence, and querying of locally generated autonomous AWD irrigation decisions and execution state reported by edge nodes without cloud recalculation.

## ADDED Requirements

### Requirement: System SHALL ingest and store edge-generated autonomous irrigation decisions
The system SHALL ingest, validate, and persist autonomous irrigation decision payloads reported by edge nodes, capturing telemetry inputs, crop stage, confidence score, decision type, trigger reason/context, requested target volume/duration, execution status, timestamps, actual execution duration, failure reason, config version, and correlation ID.

#### Scenario: Edge node reports autonomous irrigation decision
- **WHEN** edge node submits `POST /api/v1/irrigation/decisions` with a valid decision payload
- **THEN** system validates mandatory parameters, persists the decision record without cloud recalculation, returns HTTP 201 Created with the persisted decision ID, and publishes an `IRRIGATION_DECISION_MADE` system event

#### Scenario: Reject invalid irrigation decision report
- **WHEN** edge node submits decision payload missing node identity, negative requested duration, or invalid execution status
- **THEN** system rejects request with HTTP 400 Bad Request and descriptive validation error message

### Requirement: System SHALL query irrigation decision history and details
The system SHALL provide REST endpoints for querying decision history with filtering (by node, status, or date range) and retrieving detailed decision records.

#### Scenario: Query irrigation decision history
- **WHEN** client requests `GET /api/v1/irrigation/decisions` with optional filter parameters
- **THEN** system returns a paginated list of matching decision records including decision types, trigger contexts, and execution summaries

#### Scenario: Retrieve specific irrigation decision details
- **WHEN** client requests `GET /api/v1/irrigation/decisions/{id}`
- **THEN** system returns full decision details including telemetry inputs, confidence metrics, and execution timelines

### Requirement: System SHALL summarize active field irrigation status
The system SHALL aggregate current irrigation execution state across all zones in a field to expose field-level active irrigation status.

#### Scenario: Retrieve active field irrigation status
- **WHEN** client requests `GET /api/v1/irrigation/field/{fieldId}/status`
- **THEN** system returns current active irrigation status (whether any node is currently irrigating), total volume delivered today, and the list of recent decision summaries

