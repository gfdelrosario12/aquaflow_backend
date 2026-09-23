# Proposal: AquaFlow Edge Persistence Model

## Why

AquaFlow's transition to an autonomous edge irrigation architecture requires a robust, scalable relational persistence model in PostgreSQL/JPA. Currently, basic data structures lack dedicated entities for edge-originated decisions, distributed configuration, monitoring points, and field organization. Without a structured persistence layer, the system cannot reliably track edge node health, telemetry history, autonomous node-made decisions, or distributed threshold configurations.

This change establishes the full JPA domain entity graph, repository contracts, PostgreSQL indexes for time-series telemetry, optimistic locking for distributed configs, and Flyway migration scripts to support autonomous edge operations while ensuring entities are never directly exposed through REST controllers.

## What Changes

- **New JPA Entities & Enums**: Introduce `Field`, `MonitoringZone`, `EdgeNode`, `MonitoringPoint`, `TelemetryReading`, `IrrigationDecision`, `AutoIrrigationConfig`, `IrrigationAuditLog`, and related enums (`NodeStatus`, `HealthState`, `DecisionType`, `TriggerReason`, `IrrigationMode`, `SensorType`).
- **PostgreSQL Flyway Migrations**: Create migration script (`V2__edge_irrigation_schema.sql`) establishing tables, foreign keys, unique constraints, and B-tree/composite indexes optimized for time-series queries on telemetry and decisions.
- **Spring Data JPA Repositories**: Create repository interfaces for all new entities supporting pagination, time-range queries, active configuration lookups, and status filtering.
- **Optimistic Locking & Auditing**: Add `@Version` for concurrency control on mutable state (`EdgeNode`, `AutoIrrigationConfig`) and standard `@PrePersist` / `@PreUpdate` audit timestamp fields (`createdAt`, `updatedAt`).
- **DTO Isolation & Controller Boundaries**: Ensure all entities remain encapsulated within persistence and domain layers, mapped to DTOs via DtoMapper before API layer return.

## Capabilities

### New Capabilities

*(None - extending existing domain specifications)*

### Modified Capabilities

- `data-persistence`: Extends JPA entity model, Flyway migrations, database constraints, time-series indexing, and concurrency control for edge irrigation entities.
- `edge-irrigation-core`: Establishes persistence contracts for edge-originated decisions, distributed threshold configurations, and telemetry history.
- `device-management`: Establishes persistence schema for edge node identity, lifecycle, assignment, firmware, and health status.

## Impact

- **Database**: New Flyway migration `V2__edge_irrigation_schema.sql` adding tables, indexes, and FK constraints in PostgreSQL/H2.
- **Persistence Layer**: New JPA entities in `com.aquaflow.backend.entity` and Spring Data repositories in `com.aquaflow.backend.persistence`.
- **Domain & API Layer**: DTO mapping updates ensuring entities are strictly encapsulated within backend layers.

