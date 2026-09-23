## Why

AquaFlow needs a structured domain and REST API layer for managing field topology—representing physical fields, read-oriented monitoring zones, and localized monitoring points. Frontend dashboards and cloud services require read-optimized field hierarchy views and validation rules that enforce strict node-to-zone and node-to-point assignments without exposing database entities directly.

## What Changes

- **Field Topology Entities & Domain Services**: Implement Spring Data JPA entities, repositories, and domain services for `Field`, `MonitoringZone`, and `MonitoringPoint`.
- **Validation Rules & Assignment Enforcements**: Enforce structural constraints prohibiting orphaned monitoring points, overlapping/invalid node assignments, and inconsistent zone hierarchy bounds.
- **Read-Optimized Topology & Dashboard DTOs**: Create DTO response representations and mapper methods optimized for frontend field hierarchy rendering.
- **Field & Zone Management REST Endpoints**: Add REST controllers in `com.aquaflow.backend.api` for listing fields, retrieving field details with nested zones, retrieving zone details, updating crop growth stages, updating AWD water management profiles, and fetching complete topology trees.

## Capabilities

### New Capabilities
- `field-topology`: Management of fields, monitoring zones, monitoring points, crop growth stage tracking, AWD profiles, and read-optimized topology queries.

### Modified Capabilities
- `edge-irrigation-core`: Added assignment integrity rules linking edge nodes to fields, monitoring zones, and monitoring points.

## Impact

- **New REST API Endpoints**:
  - `GET /api/v1/fields`
  - `GET /api/v1/fields/{id}`
  - `GET /api/v1/fields/{id}/topology`
  - `GET /api/v1/zones/{id}`
  - `PUT /api/v1/zones/{id}/crop-stage`
  - `PUT /api/v1/zones/{id}/awd-profile`
- **Source Code**:
  - Entities: `com.aquaflow.backend.entity.Field`, `MonitoringZone`, `MonitoringPoint`
  - Repositories: `com.aquaflow.backend.persistence.FieldRepository`, `MonitoringZoneRepository`, `MonitoringPointRepository`
  - Services: `com.aquaflow.backend.domain.FieldService`, `FieldServiceImpl`, `ZoneTopologyService`
  - Controllers: `com.aquaflow.backend.api.FieldController`, `ZoneController`
  - DTOs: `FieldResponse`, `MonitoringZoneResponse`, `MonitoringPointResponse`, `FieldTopologyResponse`, `UpdateCropStageRequest`, `UpdateAwdProfileRequest`

