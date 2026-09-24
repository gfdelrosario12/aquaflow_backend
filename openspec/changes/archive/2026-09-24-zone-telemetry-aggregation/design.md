## Context

See `proposal.md` for background motivation. AquaFlow collects raw edge node telemetry (`TelemetryReading`) via LoRaWAN/HTTP. To support zone-level irrigation scheduling and dashboard monitoring, raw node measurements must be aggregated into zone-level metrics with spatial weighting, staleness detection, and composite health scoring.

## Goals / Non-Goals

**Goals:**
- Implement `ZoneAggregationService` to derive zone telemetry metrics and health scores.
- Support spatial weighting based on node spatial weight parameters.
- Provide deterministic staleness (reading age > 30 min) and offline node handling.
- Persist/cache derived zone state (`ZoneTelemetryAggregate`) for high-performance dashboard queries without altering raw historical sensor data.
- Provide `GET /api/v1/fields/{fieldId}/zones/{zoneId}/trend` endpoint for time-series trend analysis.

**Non-Goals:**
- Replacing or modifying raw `TelemetryReading` raw storage.
- Implementing edge node actuation directly (handled by autonomous edge core / irrigation controller).

## Decisions

### Decision 1: Domain-level Zone Aggregation Service
- **Choice**: Implement `ZoneAggregationService` in `com.aquaflow.backend.domain` backed by a `ZoneTelemetryAggregateRepository`.
- **Rationale**: Isolates aggregation math, spatial weighting, and health status computation from controller logic and raw ingestion pipelines.
- **Alternatives Considered**: Computing zone aggregates on-the-fly inside SQL queries (lacks support for complex spatial weighting and health rules) vs calculating in edge devices (edge nodes only see local sensor readings, not zone-wide state).

### Decision 2: Spatial Weighting and Staleness Rules
- **Choice**: Nodes assigned to a zone are filtered by active status. Readings within 30 minutes are considered fresh. If node spatial weights ($W_i$) are present, compute weighted average $\frac{\sum (V_i \times W_i)}{\sum W_i}$; otherwise use arithmetic mean $\frac{\sum V_i}{N}$.
- **Rationale**: Accounts for non-uniform sensor distribution across large field monitoring zones while preventing stale node values from biasing live irrigation decisions.

### Decision 3: Zone Health Status State Machine
- **Choice**: Evaluate composite zone health (`OPTIMAL`, `ATTENTION_REQUIRED`, `CRITICAL`, `OFFLINE`):
  - `OFFLINE`: 0 active/online nodes available.
  - `CRITICAL`: Soil moisture below wilting point, water level below critical threshold, or battery < 3.0V.
  - `ATTENTION_REQUIRED`: Any node stale, battery < 3.4V, or RSSI < -115 dBm.
  - `OPTIMAL`: All active nodes online and sensor values within target ranges.

## Risks / Trade-offs

- **[Risk] High query latency for historical trend endpoint** → **Mitigation**: Implement time bucket grouping (hourly / daily sampling) when querying historical telemetry ranges.
- **[Risk] Unassigned or missing nodes in a newly created zone** → **Mitigation**: Return empty/null telemetry aggregate with `OFFLINE` status and clear diagnostic messaging (`totalNodes = 0`).

## Migration Plan

1. Create `ZoneTelemetryAggregate` entity, repository, and DTOs.
2. Implement `ZoneAggregationService` and unit tests.
3. Update `ZoneController` to expose trend and current telemetry endpoints.
4. Run regression test suite to ensure zero impact on existing topology and ingestion endpoints.

