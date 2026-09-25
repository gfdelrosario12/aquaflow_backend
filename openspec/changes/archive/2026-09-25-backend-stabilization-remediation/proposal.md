## Why

Following the implementation of manual controls, audit logging, node health monitoring, state machines, and security hardening, a comprehensive stabilization and remediation pass is required to verify all backend systems, remove remaining dead or legacy centralized irrigation code, eliminate contract mismatches, fix concurrency/transactional edge cases, and ensure total compliance with the architectural invariant that production irrigation decisions are made autonomously by edge nodes.

## What Changes

- **Edge Autonomy Invariant Verification**: Audit all services to guarantee the cloud never independently makes autonomous irrigation decisions or takes over irrigation when edge nodes are offline. Edge nodes remain solely responsible for local AWD policy execution and physical valve operation.
- **Dead & Legacy Code Cleanup**: Remove obsolete centralized irrigation schedule evaluation logic, unused legacy helpers, and temporary workaround implementations.
- **Entity, JPA & Persistence Hardening**: Audit database relations, indexes, lazy loading boundaries, transactional constraints, and cascade rules across `Field`, `MonitoringZone`, `MonitoringPoint`, `EdgeNode`, `IrrigationDecision`, `DownlinkQueueItem`, `IrrigationCommandRecord`, and `CommandStateTransitionHistory`.
- **API Contract & DTO Alignment**: Fix inconsistent DTO mappings, pagination metadata, error codes, null handling, and response structures.
- **Telemetry Ingestion & Downlink Queueing Remediation**: Fix frame counter duplicate handling, payload decoding error recovery, downlink status mapping, retry backoff timeouts, and edge acknowledgement processing.
- **Node Health & Freshness Scheduler Synchronization**: Verify `NodeHealthMonitor` and `TelemetryFreshnessChecker` scheduler execution, event publishing, state transition persistence, and concurrency isolation.
- **Comprehensive Test Suite Expansion**: Expand test suite to verify end-to-end telemetry ingestion, downlink command queuing/delivery/ack cycles, state machine transitions, node health changes, audit logging, WebSocket publication, and error handling failure paths.

## Capabilities

### Modified Capabilities
- `edge-irrigation-core`: Hardens autonomous edge decision reporting, topology validation, and guarantees cloud backend never overrides edge node autonomy during offline or critical states.
- `observability`: Fixes real-time event publication, structured audit logs, WebSocket channel delivery, and telemetry trend calculations.

## Impact

- Domain & Services: Refactors `IrrigationDecisionServiceImpl`, `DownlinkQueueServiceImpl`, `NodeHealthMonitor`, `TelemetryIngestionServiceImpl`, and `ManualControlServiceImpl`.
- Persistence: Verifies JPA annotations, transactional boundaries, and repository query optimizations.
- Testing: Expands integration and unit test coverage to ensure 100% build pass rate.

