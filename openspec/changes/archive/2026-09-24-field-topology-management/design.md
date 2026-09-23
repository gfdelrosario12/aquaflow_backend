## Context

The AquaFlow backend baseline has PostgreSQL entities and repositories for `Field`, `MonitoringZone`, `EdgeNode`, `MonitoringPoint`, `TelemetryReading`, `IrrigationDecision`, and `AutoIrrigationConfig`. However, domain services, validation rules, DTO mapping boundaries, and REST APIs for managing field topology and querying frontend-optimized hierarchy trees have not yet been built.

See `proposal.md` for the underlying business motivation.

## Goals / Non-Goals

**Goals:**
- Implement `FieldService`, `ZoneService`, and `FieldTopologyService` encapsulation layers.
- Build read-optimized field hierarchy and topology DTO representations (`FieldTopologyResponse`, `MonitoringZoneTopologyResponse`, `MonitoringPointResponse`).
- Implement REST API controllers `FieldController` and `ZoneController` under `/api/v1/fields` and `/api/v1/zones`.
- Implement endpoints for updating crop growth stage (`PUT /api/v1/zones/{id}/crop-stage`) and AWD profile (`PUT /api/v1/zones/{id}/awd-profile`).
- Enforce valid topology hierarchy constraints (rejecting orphaned monitoring points or invalid zone-field cross assignments).

**Non-Goals:**
- Directly streaming live WebSocket telemetry inside topology endpoints (telemetry streaming belongs in dedicated MQTT/WebSocket handlers).
- Geographic GIS spatial index calculations beyond storing and serving GeoJSON boundary strings.

## Decisions

### Decision 1: Dedicated Read-Optimized Topology Service (`FieldTopologyService`)
- **Rationale**: Fetching complete field topology for frontend dashboards requires joining Fields, MonitoringZones, MonitoringPoints, and EdgeNodes. Combining this into a single query/service method avoids N+1 database queries while preserving service layer encapsulation.
- **Alternatives Considered**: Fetching individual entities in separate REST calls from the client (rejected due to frontend latency and multiple HTTP roundtrips).

### Decision 2: Encapsulated DTO Interfaces (No JPA Entity Exposure)
- **Rationale**: Controllers must consume and return dedicated DTOs (`FieldRequest`, `FieldResponse`, `FieldTopologyResponse`, `UpdateCropStageRequest`, `UpdateAwdProfileRequest`) mapped via `DtoMapper`. JPA entities never leave the service boundary.
- **Alternatives Considered**: Exposing `@Entity` classes directly with Jackson annotations (rejected to prevent leaky domain abstraction and circular serialization errors).

### Decision 3: Explicit Enums for Crop Growth Stages and AWD Profiles
- **Rationale**: Crop growth stage transitions (e.g., `VEGETATIVE`, `REPRODUCTIVE`, `RIPENING`) and AWD profiles drive automated threshold recalculations. Representing them as strongly-typed enums/value objects prevents invalid string payloads.
- **Alternatives Considered**: Free-form string fields in DTOs (rejected due to runtime validation risks).

## Risks / Trade-offs

- **Risk**: Deeply nested topology structures causing large JSON payload sizes for large farms.
  - **Mitigation**: Support paginated field summaries on `GET /api/v1/fields` and fetch detailed nested topology only when requesting `GET /api/v1/fields/{id}/topology`.
- **Risk**: Concurrent topology updates (e.g. reassigning a node while creating a monitoring point).
  - **Mitigation**: Use `@Transactional` boundaries in services and optimistic locking on assigned `EdgeNode` entities.

## Migration Plan

1. Implement DTO requests/responses in `com.aquaflow.backend.dto`.
2. Implement domain interfaces and service implementations (`FieldService`, `FieldServiceImpl`, `ZoneService`, `ZoneServiceImpl`, `FieldTopologyService`).
3. Add REST controllers `FieldController` and `ZoneController` in `com.aquaflow.backend.api`.
4. Create comprehensive unit tests for service validation and controller endpoints.
5. Verify build with `mvn clean test`.

