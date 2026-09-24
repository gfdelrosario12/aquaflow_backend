## Context

The AquaFlow backend tracks multiple event categories: automated decisions, manual commands, configuration synchronization, telemetry ingestion, and node health. A unified, structured audit logging framework is required to persist immutable system and irrigation audit records across all operational domains.

See `proposal.md` for overall motivation and capabilities.

## Goals / Non-Goals

**Goals:**
- Implement `SystemAuditLog` domain model and `SystemAuditLogRepository` for operational, security, and hardware event logging.
- Enhance `IrrigationAuditLog` and `IrrigationAuditLogRepository` for domain-specific irrigation event logging.
- Implement `AuditLogService` for writing structured audit logs, querying logs with filters/pagination, and generating CSV/JSON export files.
- Expose REST API endpoints under `/api/v1/audit/`:
  - `GET /api/v1/audit/irrigation`
  - `GET /api/v1/audit/system`
  - `GET /api/v1/audit/export`
- Integrate event listeners to automatically write audit records on `SystemEvent` occurrences (node status changes, alarms, emergency stops, config syncs).
- Enforce immutability by omitting any mutation endpoints for audit logs.

**Non-Goals:**
- Modifying underlying database engine rules outside the Spring application data layer.
- Storing high-frequency raw telemetry sensor samples in audit tables; audit tables store event and operational metadata.

## Decisions

### Decision 1: Dual Audit Log Entities (`IrrigationAuditLog` and `SystemAuditLog`)
- **Choice**: Maintain separate JPA entities and tables (`irrigation_audit_logs` and `system_audit_logs`) while unifying query/export abstractions under `AuditLogService`.
- **Rationale**: Keeps domain-specific irrigation audit records logically isolated from high-volume system/security operational logs while enabling optimized queries for each.
- **Alternatives Considered**: Single monolithic audit table (rejected: high indexing overhead and mixed query concerns).

### Decision 2: Event Listener Integration
- **Choice**: Integrate `SystemEventListener` and `AuditLogService` so that published system events (`NODE_STATUS_CHANGED`, `EMERGENCY_STOP_ACTIVATED`, `ALARM_TRIGGERED`, `CONFIG_SYNCED`) automatically generate immutable system/irrigation audit log records.
- **Rationale**: Guarantees zero missed audit events across asynchronous subsystems without tight coupling in business logic.
- **Alternatives Considered**: Manual logging calls in every service method (rejected: error-prone and easy to omit during refactoring).

### Decision 3: Export Formats (CSV and JSON)
- **Choice**: Implement streaming export support in `AuditLogService` for both CSV and JSON formats based on HTTP query parameters (`?format=csv` or `?format=json`).
- **Rationale**: Provides flexibility for compliance reporting (CSV for spreadsheets, JSON for SIEM / external log ingestion tools).

## Risks / Trade-offs

- **[Risk]** Audit log table growth over time.
  - **Mitigation**: Database indexing on `createdAt`, `eventType`, `actor`, `entityId`, and pagination enforced on all query endpoints.

