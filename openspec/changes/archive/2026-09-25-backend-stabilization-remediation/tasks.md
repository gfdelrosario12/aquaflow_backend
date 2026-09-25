## 1. Code Cleanup & Edge Autonomy Invariant Audit

- [x] 1.1 Perform static audit across domain services ensuring no cloud background process overrides offline edge node local AWD behavior
- [x] 1.2 Remove any legacy centralized irrigation logic or obsolete workaround code
- [x] 1.3 Ensure event publication and audit logging calls are isolated from primary database transactions via try-catch guards

## 2. Persistence, Entities & DTO Consistency

- [x] 2.1 Audit JPA entities (`Field`, `MonitoringZone`, `EdgeNode`, `IrrigationDecision`, `DownlinkQueueItem`) for relationship mappings, lazy loading, and cascade rules
- [x] 2.2 Verify DTO mappers and JSON serialization handle `LocalDateTime` and null fields consistently

## 3. End-to-End & Integration Test Coverage Expansion

- [x] 3.1 Write end-to-end integration test verifying full telemetry ingestion -> health status evaluation -> audit log -> WebSocket publication pipeline
- [x] 3.2 Write integration test verifying configuration synchronization flow -> downlink queueing -> edge ACK -> state machine completion
- [ ] 3.3 Execute full clean test suite (`mvn clean test`) to verify 100% build pass rate
- [x] 3.3 Execute full clean test suite (`mvn clean test`) to verify 100% build pass rate

