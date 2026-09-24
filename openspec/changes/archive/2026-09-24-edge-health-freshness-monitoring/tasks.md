## 1. Domain Models & DTOs

- [x] 1.1 Update `NodeHealthMetrics` / `EdgeNode` to capture health status, signal metrics, battery levels, and last seen timestamps
- [x] 1.2 Create `NodeHealthResponse` and `NodeHealthSummaryResponse` DTOs

## 2. Scheduler Services & Event Dispatching

- [x] 2.1 Implement `NodeHealthMonitor` scheduled service to evaluate health thresholds, persist health transitions, and publish `NODE_STATUS_CHANGED` system events
- [x] 2.2 Implement `TelemetryFreshnessChecker` scheduled service to detect stale telemetry and publish `ALARM_TRIGGERED` system events
- [x] 2.3 Ensure offline or critical health states preserve local edge node autonomy without triggering cloud valve overrides

## 3. REST API Endpoints

- [x] 3.1 Expose `GET /api/v1/devices/nodes/health` in controller to retrieve health summary across all nodes
- [x] 3.2 Expose `GET /api/v1/devices/nodes/{id}/health` in controller to retrieve detailed health metrics for a specific node

## 4. Verification & Testing

- [x] 4.1 Create unit tests for `NodeHealthMonitor` and `TelemetryFreshnessChecker`
- [x] 4.2 Create controller tests for edge node health monitoring REST endpoints
- [x] 4.3 Verify full project compilation and execute all tests using `mvn clean compile` and `mvn test`

