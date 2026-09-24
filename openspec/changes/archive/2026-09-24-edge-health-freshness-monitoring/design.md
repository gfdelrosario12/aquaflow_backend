## Context

AquaFlow edge nodes communicate via LoRaWAN and run autonomous AWD irrigation logic. Hardware degradation, low battery, weak RF signal, or physical severance can impair communication. Periodic background monitoring is required to evaluate node health and telemetry freshness without compromising edge autonomy.

See `proposal.md` for overall motivation and capabilities.

## Goals / Non-Goals

**Goals:**
- Implement `NodeHealthMonitor` to periodically evaluate node metrics (battery level, RSSI, SNR, last seen, config sync status) and update `HealthState` (`HEALTHY`, `DEGRADED`, `CRITICAL`, `OFFLINE`).
- Implement `TelemetryFreshnessChecker` to monitor telemetry reading age against a freshness threshold (30 minutes) and report stale telemetry alarms.
- Publish realtime system events (`NODE_STATUS_CHANGED`, `ALARM_TRIGGERED`) when health transitions occur or stale telemetry is detected.
- Expose REST API endpoints under `/api/v1/devices/nodes/health` and `/api/v1/devices/nodes/{id}/health` for inspecting node health status and metrics history.
- Ensure cloud health monitoring does NOT trigger remote cloud override of local edge valve policies when a node goes offline.

**Non-Goals:**
- Taking over physical valve timing from the cloud when a node goes offline.
- Overriding hardware safety interlocks built into edge microcontrollers.

## Decisions

### Decision 1: Scheduled Health & Freshness Evaluation
- **Choice**: Implement two dedicated `@Scheduled` background tasks: `NodeHealthMonitor` (every 60s) and `TelemetryFreshnessChecker` (every 60s).
- **Rationale**: Decouples heavy health auditing from realtime telemetry uplink handling.
- **Alternatives Considered**: In-line evaluation during telemetry uplink processing (rejected: cannot detect offline or silent nodes that stop transmitting).

### Decision 2: Explicit Health Threshold Matrix
- **Choice**:
  - `HEALTHY`: Battery >= 20%, RSSI >= -115 dBm, last seen < 30 mins ago.
  - `DEGRADED`: Battery 10% - 19% OR RSSI < -115 dBm OR last seen 30 - 60 mins ago.
  - `CRITICAL`: Battery < 10% OR consecutive failures >= 3.
  - `OFFLINE`: Last seen > 60 mins ago.
- **Rationale**: Provides predictable, deterministic state transitions for operators and dashboard alerts.

### Decision 3: Edge Local Autonomy Guarantee
- **Choice**: Maintain strict policy that node health state changes (including `OFFLINE`) do not trigger cloud irrigation execution overrides.
- **Rationale**: Local edge nodes hold physical hardware safety interlocks and run local AWD policies independently.

## Risks / Trade-offs

- **[Risk]** Intermittent LoRaWAN connectivity causes transient `DEGRADED` or `OFFLINE` flaps.
  - **Mitigation**: Require consecutive evaluation failures before transitioning to `CRITICAL` or `OFFLINE`, and publish events only on state change.

