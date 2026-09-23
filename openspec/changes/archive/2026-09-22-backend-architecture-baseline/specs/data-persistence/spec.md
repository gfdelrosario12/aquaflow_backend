## Purpose

Define the data persistence layer including JPA entities, Spring Data repositories, Flyway database migrations, and multi-profile datasource configuration.

## ADDED Requirements

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
