# Design: AquaFlow Edge Persistence Model

## Context

See `proposal.md` for motivation. AquaFlow is transitioning to an autonomous edge architecture where edge nodes (microcontroller/embedded gateways) execute localized irrigation logic independently of continuous cloud connectivity. The database layer must accurately model the field hierarchy, edge node communication identity, historical telemetry, node-made decisions, distributed configurations, and audit events.

## Goals / Non-Goals

**Goals:**
- Design a normalized, production-grade PostgreSQL JPA entity model for autonomous edge operations.
- Establish explicit foreign key relationships, unique constraints, and composite indexes optimized for time-series queries.
- Ensure optimistic locking on mutable configuration and state entities (`EdgeNode`, `AutoIrrigationConfig`).
- Provide Flyway migration script `V2__edge_irrigation_schema.sql` supporting H2 and PostgreSQL execution.
- Maintain strict encapsulation where JPA entities are never exposed directly to REST clients.

**Non-Goals:**
- Replace existing legacy baseline tables (`zones`, `crops`, `devices`, `alerts`) immediately—new edge entities live alongside existing entities with migration pathways.
- Build edge MQTT broker communication protocol handlers in this change (handled in edge integration change).

## Decisions

### 1. Entity Model Hierarchy & Read-Oriented Aggregation

**Decision:**
- `Field`: Top-level agricultural location entity (name, location boundaries/coordinates, owner).
- `MonitoringZone`: Read-oriented aggregation grouping assigned `EdgeNode` instances and `MonitoringPoint` instances within a `Field`.
- `EdgeNode`: Represents the physical autonomous field gateway/device. Contains communication identity (MAC address, serial number, hardware model), lifecycle state (`NodeLifecycleState`), health status (`HealthState`), assigned `MonitoringZone`, firmware version, and last heartbeat timestamp. Uses `@Version` for concurrency control.
- `MonitoringPoint`: Location within a zone containing sensors or actuators (soil probe depth, flow meter location).
- `TelemetryReading`: Immutable time-series sensor reading associated with a `MonitoringPoint` and `EdgeNode` (sensor type, double value, unit, timestamp).
- `IrrigationDecision`: Immutable record of autonomous decisions made *by* the edge node (decision type, trigger reason, duration, water volume, status, node timestamp).
- `AutoIrrigationConfig`: Distributed configuration per node/zone (moisture thresholds, max duration, schedule mode, safety override parameters). Uses `@Version` for optimistic locking during cloud-to-edge distribution.
- `IrrigationAuditLog`: Immutable audit trail for configuration updates, manual overrides, and node state transitions.

**Alternatives Considered:**
- Storing telemetry as unindexed JSONB blobs: Rejected due to query latency for time-series range aggregations.
- Cloud-invented decisions: Rejected because edge nodes operate autonomously when offline; decisions must be recorded from the edge's perspective.

### 2. Time-Series Indexing Strategy

**Decision:**
Construct composite B-Tree indexes in Flyway migration `V2__edge_irrigation_schema.sql`:
- `idx_telemetry_point_time`: `(monitoring_point_id, timestamp DESC)` for fast sensor history retrieval.
- `idx_decision_node_time`: `(edge_node_id, node_timestamp DESC)` for node decision audit trails.
- `idx_node_zone_status`: `(zone_id, status)` for quick zone node health summaries.
- `idx_audit_entity_time`: `(entity_type, entity_id, created_at DESC)` for audit trail queries.

### 3. Concurrency Control & Audit Timestamps

**Decision:**
- Apply `@Version private Long version;` on `EdgeNode` and `AutoIrrigationConfig` to prevent lost updates when cloud operators and edge synchronization processes concurrently touch configuration or node state.
- Apply standard `@Column(name = "created_at", updatable = false)` and `@Column(name = "updated_at")` managed via JPA lifecycle callbacks (`@PrePersist` and `@PreUpdate`).

### 4. DTO Isolation Boundary

**Decision:**
- Controllers return only DTO responses (`EdgeNodeResponse`, `TelemetryReadingResponse`, `IrrigationDecisionResponse`, `AutoIrrigationConfigResponse`, etc.).
- JPA entities are consumed exclusively by repositories and domain services.

## Risks / Trade-offs

- **[Time-Series Scale]** → High frequency telemetry ingestion may increase database size over time.  
  *Mitigation:* Create composite indexes `(monitoring_point_id, timestamp DESC)` and prepare table partitioning strategy for future Flyway migrations.
- **[Concurrent Edge Config Updates]** → Simultaneous updates from UI and edge sync could trigger `OptimisticLockException`.  
  *Mitigation:* Handle `OptimisticLockException` in `GlobalExceptionHandler` returning structured `CONCURRENCY_CONFLICT` (HTTP 409) error responses.

