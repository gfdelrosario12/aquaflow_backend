## Context

See `proposal.md` for background motivation. AquaFlow uses edge computing for autonomous irrigation. The cloud platform acts as policy repository managing `AutoIrrigationConfig` and `AwdThresholdConfig` without executing irrigation decisions directly.

## Goals / Non-Goals

**Goals:**
- Provide full CRUD and version management for `AutoIrrigationConfig` and `AwdThresholdConfig`.
- Implement `AwdConfigValidator` for strict bounds checking and parameter safety.
- Record audit trails (`updatedBy`, `changeReason`, `updatedAt`, `configVersion`).
- Expose REST endpoints under `/api/v1/fields/{fieldId}/awd-config`.
- Emit `CONFIG_SYNCED` events when policy versions change.

**Non-Goals:**
- Direct actuation control loops in the cloud service (handled by autonomous edge nodes).

## Decisions

### Decision 1: Domain-level Configuration Models & Repositories
- **Choice**: Implement `AutoIrrigationConfig` (mapped to `auto_irrigation_configs`) and `AwdThresholdConfig` (mapped to `awd_threshold_configs`) in `com.aquaflow.backend.entity`.
- **Rationale**: Isolates policy parameters cleanly, allowing one-to-many relationship with growth-stage specific threshold sets (`VEGETATIVE`, `REPRODUCTIVE`, `RIPENING`, `FALLOW`).

### Decision 2: Monotonic Versioning & Audit Metadata
- **Choice**: Maintain a `configVersion` Long field incremented on every valid configuration update alongside `updatedBy` (user username or principal) and `changeReason`.
- **Rationale**: Edge nodes use `configVersion` to verify local cache freshness.

### Decision 3: Parameter Safety Validation Rules
- **Choice**: `AwdConfigValidator` validates:
  - `0 < maxDurationMinutes <= 1440`
  - `0 <= minCooldownMinutes <= 1440`
  - `0 <= allowedStartHour, allowedEndHour < 24`
  - `0.0 <= minConfidenceThreshold <= 1.0`
  - `triggerMoisturePercentage <= targetMoisturePercentage`
- **Rationale**: Prevents erroneous user inputs from triggering invalid edge node behaviors.

## Risks / Trade-offs

- **[Risk] Uninitialized configuration for a new field** → **Mitigation**: Return standard default AWD configuration template if no existing config is found for fieldId.

## Migration Plan

1. Create `CropGrowthStage` enum, entities, and repositories.
2. Implement `AwdConfigValidator`, `AwdConfigService`, and `AwdConfigServiceImpl`.
3. Create `AwdConfigController` mapped to `/api/v1/fields/{fieldId}/awd-config`.
4. Run unit and integration test suite.

