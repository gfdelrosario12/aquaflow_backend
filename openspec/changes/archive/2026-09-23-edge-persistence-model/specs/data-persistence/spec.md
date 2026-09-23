## ADDED Requirements

### Requirement: System SHALL define autonomous edge persistence entities
The system SHALL define JPA entity models for `Field`, `MonitoringZone`, `EdgeNode`, `MonitoringPoint`, `TelemetryReading`, `IrrigationDecision`, `AutoIrrigationConfig`, and `IrrigationAuditLog` with explicit foreign key relationships, optimistic locking versioning (`@Version`), and `@PrePersist`/`@PreUpdate` audit timestamps.

#### Scenario: Edge entity model mapping
- **WHEN** JPA entity manager initializes
- **THEN** all autonomous edge entities map to their corresponding PostgreSQL tables with constraint validations and versioning fields

### Requirement: System SHALL provide repository interfaces for edge domain entities
The system SHALL provide Spring Data JPA repository interfaces for `FieldRepository`, `MonitoringZoneRepository`, `EdgeNodeRepository`, `MonitoringPointRepository`, `TelemetryReadingRepository`, `IrrigationDecisionRepository`, `AutoIrrigationConfigRepository`, and `IrrigationAuditLogRepository` supporting time-series range queries, pagination, and status filters.

#### Scenario: Query telemetry readings by time range
- **WHEN** repository method `findByMonitoringPointIdAndTimestampBetween` is called
- **THEN** system returns paginated historical telemetry readings within the specified time range sorted by timestamp descending

### Requirement: System SHALL implement Flyway schema migration for autonomous edge model
The system SHALL include a migration script (`V2__edge_irrigation_schema.sql`) creating tables, foreign keys, unique constraints, and composite indexes for efficient time-series queries.

#### Scenario: Apply V2 schema migration
- **WHEN** application executes Flyway migration on PostgreSQL or H2
- **THEN** all tables, foreign key constraints, unique constraints, and composite indexes are applied successfully without errors

