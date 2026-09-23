# data-persistence Specification

## Purpose
TBD - created by archiving change backend-architecture-baseline. Update Purpose after archive.
## Requirements
### Requirement: System SHALL define JPA entity models
The system SHALL define entity classes for Zone, Crop, IrrigationSchedule, SensorReading, Device, Alert, User, and Schedule.

#### Scenario: Entity mapping
- **WHEN** application starts
- **THEN** all entities are correctly mapped to database tables with relationships

### Requirement: System SHALL provide Spring Data JPA repositories
The system SHALL provide repository interfaces for all entities with standard CRUD and custom query methods.

#### Scenario: Query schedules by zone
- **WHEN** repository method `findByZoneIdOrderByStartTimeAsc` is called
- **THEN** system returns all schedules for the zone ordered by start time

### Requirement: System SHALL support Flyway database migrations
The system SHALL use Flyway for version-controlled schema creation and updates across environments.

#### Scenario: Apply migrations on startup
- **WHEN** application starts with a fresh database
- **THEN** Flyway applies all pending migrations in order

### Requirement: System SHALL configure datasource per profile
The system SHALL use H2 in-memory database for dev/test profiles and PostgreSQL for prod profile.

#### Scenario: Dev profile datasource
- **WHEN** application runs with `dev` profile active
- **THEN** system connects to H2 in-memory database with auto-ddl enabled

### Requirement: System SHALL support Flyway migrations for PostgreSQL
The system SHALL include Flyway migration scripts compatible with PostgreSQL for production deployments.

#### Scenario: PostgreSQL migration
- **WHEN** application runs with `prod` profile
- **THEN** Flyway applies PostgreSQL-compatible migration scripts

### Requirement: System SHALL configure database connection pool per profile
Each profile SHALL have appropriate connection pool settings for optimal resource usage.

#### Scenario: Connection pool configuration
- **WHEN** application starts with specific profile
- **THEN** connection pool is configured with profile-appropriate settings

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

