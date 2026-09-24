## Why

AquaFlow edge nodes perform autonomous Alternate Wetting and Drying (AWD) irrigation calculations locally based on configured policy rules and real-time field sensors. To maintain central observability, auditability, and historical decision tracking across multi-zone operations, edge nodes must report their locally generated decisions back to the cloud platform. 

The backend platform must record, store, and expose these reported decisions (including telemetry inputs, crop stage, confidence metrics, trigger reasons, requested targets, execution status, timestamps, actual durations, failure reasons, configuration version, and correlation ID) without attempting to recalculate or override the edge node's local decision during normal operation.

## What Changes

- Create domain model and persistence entity `IrrigationDecision` with rich decision metadata (telemetry inputs, crop stage, confidence, decision type, trigger context, requested volume/duration, execution status, timestamps, actual execution duration, failure reason, config version, correlation ID).
- Create `IrrigationDecisionRepository` to store and query decision records by node ID, field ID, execution status, and time ranges.
- Implement `IrrigationDecisionService` to handle decision ingestion, validation, and field-level status aggregation.
- Expose REST API endpoints:
  - `POST /api/v1/irrigation/decisions` - Submit locally generated edge irrigation decision
  - `GET /api/v1/irrigation/decisions` - List/filter irrigation decision history
  - `GET /api/v1/irrigation/decisions/{id}` - Retrieve detailed decision record
  - `GET /api/v1/irrigation/field/{fieldId}/status` - Retrieve current active irrigation status and recent decision summary for a field
- Publish `IRRIGATION_DECISION_MADE` and `IRRIGATION_EXECUTION_UPDATED` system events on ingestion and execution status updates.

## Capabilities

### New Capabilities

- `autonomous-irrigation-decision-reporting`: Ingests, validates, stores, and exposes autonomous AWD irrigation decisions and execution statuses reported by edge nodes without cloud recalculation or override.

### Modified Capabilities

- `irrigation-api`: Extends irrigation endpoints to support reporting and querying edge-driven decisions and field status summaries.

## Impact

- **Domain & Persistence**: Enhances `IrrigationDecision` entity and repository in `com.aquaflow.backend.entity` and `com.aquaflow.backend.persistence`.
- **API & DTOs**: Adds `IrrigationDecisionController`, `IrrigationDecisionRequest`, `IrrigationDecisionResponse`, `FieldIrrigationStatusResponse`.
- **Event System**: Emits `IRRIGATION_DECISION_MADE` and `IRRIGATION_EXECUTION_UPDATED` system events.

