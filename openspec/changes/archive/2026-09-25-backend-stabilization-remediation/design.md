## Context

See `proposal.md` for motivation. This design details the systematic verification, code cleanup, edge autonomy invariant enforcement, transactional isolation, and comprehensive test suite validation.

## Goals / Non-Goals

**Goals:**
- Guarantee edge node autonomous decision sovereignty (cloud never generates autonomous irrigation triggers or overrides offline nodes).
- Clean up any legacy or dead centralized irrigation scheduling code.
- Verify transactional boundaries, null-safety, and exception handling across all domain services.
- Ensure all entity relationships, JPA cascade options, and database repositories function correctly.
- Expand unit and integration test coverage to guarantee 100% test pass rate across all backend modules.

**Non-Goals:**
- Introducing new functional features or redesigning core architecture.

## Decisions

### Decision 1: Edge Autonomy Invariant Enforcement
- **Choice**: Audit `IrrigationDecisionServiceImpl`, `DownlinkQueueServiceImpl`, `NodeHealthMonitor`, `TelemetryFreshnessChecker`, and `ManualControlServiceImpl`. Ensure no background job or scheduler attempts to override edge AWD policy execution when a node is marked `OFFLINE` or `DEGRADED`.

### Decision 2: Event Publication & Error Isolation
- **Choice**: Wrap all `systemEventPublisher.publish(...)` and `auditLogService.log...(...)` invocations in try-catch blocks so transient event broadcasting failures do not cause database transaction rollbacks.

### Decision 3: Comprehensive Test Coverage Matrix
- **Choice**: Add tests validating:
  - Telemetry ingestion -> Node health update -> Event publication flow
  - Downlink command queueing -> Transmission -> Edge ACK -> State machine completion
  - Emergency stop trigger -> State machine transition -> Authorization validation
  - Audit logging immutability and correlation ID propagation

## Risks / Trade-offs

- [Risk] Hidden edge cases in asynchronous event listener execution.
  → **Mitigation**: Add isolated unit tests for `SystemEventListener` and `RealtimeEventPublisherServiceImpl`.

