## Why

The AquaFlow platform requires immutable, tamper-resistant audit logging to track configuration changes, node lifecycle events, telemetry failures, autonomous decisions, manual overrides, emergency stops, downlink operations, and administrative actions for security compliance, accountability, and operational troubleshooting.

## What Changes

- Create `SystemAuditLog` domain model and repository to record non-irrigation system audit events (admin actions, node lifecycle transitions, auth events, telemetry failures, downlink events, system faults).
- Enhance `IrrigationAuditLog` domain model and service integrations to record structured irrigation audit logs (configuration changes, autonomous decisions, manual commands, emergency stops, edge acknowledgements).
- Ensure all audit entries capture structured metadata: event type, correlation ID, actor identity, timestamp, target resource, previous state, resulting state, and rationale/reasoning.
- Ensure audit log records are immutable and cannot be deleted or modified through application REST APIs.
- Expose query and export endpoints:
  - `GET /api/v1/audit/irrigation` - Query irrigation audit records with filtering and pagination.
  - `GET /api/v1/audit/system` - Query system-level audit records with filtering and pagination.
  - `GET /api/v1/audit/export` - Export filtered audit logs as CSV or JSON format.

## Capabilities

### New Capabilities
- `comprehensive-audit-logging`: Immutable system and irrigation audit logging, event capture, correlation tracking, metadata enrichment, and secure audit query/export capabilities.

### Modified Capabilities
- `observability`: Add REST endpoints for querying irrigation audit logs (`GET /api/v1/audit/irrigation`), system audit logs (`GET /api/v1/audit/system`), and exporting audit records (`GET /api/v1/audit/export`).

## Impact

- Domain & Persistence: `SystemAuditLog` entity, `SystemAuditLogRepository`, `IrrigationAuditLog` entity updates, `AuditLogService`.
- Web / REST APIs: `AuditLogController` exposing `/api/v1/audit/` endpoints.
- Integration: Event listeners and interceptors automatically capturing audit events across configuration, node lifecycle, telemetry, and security contexts.

