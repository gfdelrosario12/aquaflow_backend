## Context

See `proposal.md` for background motivation. Edge nodes execute AWD irrigation logic locally and report their decisions and execution progress to the backend. The backend must ingest, store, and expose decision records without re-evaluating or overriding the decision logic.

## Goals / Non-Goals

**Goals:**
- Enrich `IrrigationDecision` entity and `IrrigationDecisionRepository` to store full decision metadata:
  - Telemetry inputs (water level, soil moisture, temperature, battery, RSSI, SNR)
  - Crop stage (`CropGrowthStage`)
  - Confidence score ($0.0-1.0$)
  - Decision type (`DecisionType`: `START_IRRIGATION`, `STOP_IRRIGATION`, `SKIP_IRRIGATION`, `RAIN_DELAY`)
  - Trigger context / reason (`TriggerReason`: `AUTO_THRESHOLD`, `SCHEDULED`, `MANUAL_OVERRIDE`, `EMERGENCY_STOP`)
  - Requested duration (minutes) & requested volume (liters)
  - Execution status (`IN_PROGRESS`, `COMPLETED`, `ABORTED`, `FAILED`)
  - Actual execution duration (minutes)
  - Failure reason (String)
  - Config version (`configVersion`)
  - Correlation ID
- Implement `IrrigationDecisionService` & `IrrigationDecisionServiceImpl`.
- Implement `IrrigationDecisionValidator` for bound and status validations.
- Expose REST endpoints under `/api/v1/irrigation/`:
  - `POST /api/v1/irrigation/decisions`
  - `GET /api/v1/irrigation/decisions`
  - `GET /api/v1/irrigation/decisions/{id}`
  - `GET /api/v1/irrigation/field/{fieldId}/status`
- Emit `IRRIGATION_DECISION_MADE` and `IRRIGATION_EXECUTION_UPDATED` system events.

**Non-Goals:**
- Re-calculating AWD thresholds or overriding edge node decisions in the backend.

## Decisions

### Decision 1: Rich Entity Mapping with Backward Compatibility
- **Choice**: Enhance existing `IrrigationDecision` entity with optional/nullable fields for advanced metadata (`cropStage`, `confidence`, `telemetryWaterLevelCm`, `telemetrySoilMoisturePercent`, `actualDurationMinutes`, `failureReason`, `configVersion`, `correlationId`).
- **Rationale**: Retains full compatibility with existing repository calls while supporting rich telemetry and execution reporting.

### Decision 2: Decoupled Validation & Ingestion Engine
- **Choice**: Separate `IrrigationDecisionValidator` component to check parameter bounds (e.g. non-negative duration, valid confidence score range) before persistence.
- **Rationale**: Prevents dirty or invalid decision records from cluttering history while returning clear HTTP 400 validation error responses.

### Decision 3: Event Publication on State Transitions
- **Choice**: Emit `IRRIGATION_DECISION_MADE` when a decision is created, and `IRRIGATION_EXECUTION_UPDATED` when execution status or actual duration is updated.
- **Rationale**: Integrates cleanly with `RealtimeEventPublisherService` for WebSocket updates to frontend clients.

## Risks / Trade-offs

- **[Risk] High volume of decision reports from multiple edge nodes** → **Mitigation**: Database index on `(edge_node_id, created_at)` and paginated queries on `GET /api/v1/irrigation/decisions`.
- **[Risk] Unassigned edge node submitting decisions** → **Mitigation**: Resolve node by ID/DevEUI; if not found, return HTTP 404/400.

## Migration Plan

1. Update `IrrigationDecision` entity, `IrrigationDecisionRepository`, DTOs, and mapper methods.
2. Implement `IrrigationDecisionValidator`, `IrrigationDecisionService`, and `IrrigationDecisionServiceImpl`.
3. Implement `IrrigationDecisionController`.
4. Write unit and WebMvc tests and verify zero regressions.

