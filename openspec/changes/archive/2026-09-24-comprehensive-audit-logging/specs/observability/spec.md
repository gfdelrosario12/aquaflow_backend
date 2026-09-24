## ADDED Requirements

### Requirement: System SHALL expose REST endpoints for querying and exporting audit logs
The system SHALL expose REST endpoints under `/api/v1/audit/` to query irrigation audit logs (`GET /api/v1/audit/irrigation`), system audit logs (`GET /api/v1/audit/system`), and export audit records (`GET /api/v1/audit/export`).

#### Scenario: Query irrigation audit records
- **WHEN** client sends GET `/api/v1/audit/irrigation` with filter parameters
- **THEN** system returns a paginated list of irrigation audit records

#### Scenario: Query system audit records
- **WHEN** client sends GET `/api/v1/audit/system` with filter parameters
- **THEN** system returns a paginated list of system audit records

#### Scenario: Export audit records
- **WHEN** client sends GET `/api/v1/audit/export` with format parameter
- **THEN** system returns audit records formatted as CSV or JSON

