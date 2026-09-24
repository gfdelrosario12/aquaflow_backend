## Why

AquaFlow edge nodes operate in remote physical fields where communication disruptions, battery depletion, or hardware degradation can occur. Automated health monitoring and telemetry freshness detection must track offline nodes, stale telemetry, low battery levels, poor LoRaWAN signal quality, repeated communication failures, and abnormal reporting intervals while preserving local edge autonomy and safety interlocks.

## What Changes

- Implement `NodeHealthMonitor` scheduled service to evaluate node health metrics (battery percentage, RSSI, SNR, consecutive communication failures, config sync status) and transition health states (`HEALTHY`, `DEGRADED`, `CRITICAL`, `OFFLINE`).
- Implement `TelemetryFreshnessChecker` scheduled service to monitor telemetry staleness against configurable age thresholds and report stale telemetry alerts.
- Persist significant health state transitions and metrics in database.
- Publish realtime health events (`NODE_STATUS_CHANGED`, `ALARM_TRIGGERED`) via `SystemEventPublisher` and `RealtimeEventPublisherService`.
- Enforce policy that an offline node state SHALL NOT grant permission for the cloud to override or assume remote control of autonomous edge irrigation decisions; local edge safety interlocks remain authoritative.
- Expose REST API endpoints for node health summary, health history, and freshness inspection under `/api/v1/devices/nodes/health`.

## Capabilities

### New Capabilities
- `edge-health-freshness-monitoring`: Automated health monitoring, telemetry freshness detection, threshold evaluation, state transition persistence, realtime event publication, and edge autonomy preservation.

### Modified Capabilities
- `device-management`: Add endpoints for inspecting edge node health states, telemetry freshness, and health metrics history under `/api/v1/devices/nodes/health`.

## Impact

- Services: `NodeHealthMonitor`, `TelemetryFreshnessChecker`, `EdgeNodeRegistryService` updates.
- Entities: `NodeHealthMetrics`, `EdgeNode` health status update logic.
- REST Controllers: `EdgeNodeRegistryController` / `DeviceController` updated with health inspection endpoints.
- Realtime: Integration with `SystemEventPublisher` and `RealtimeEventPublisherService`.

