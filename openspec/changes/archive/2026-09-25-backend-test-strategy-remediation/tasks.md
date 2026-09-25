## 1. IDE Warning Remediation & Controller Verification

- [x] 1.1 Resolve any missing DTO or service class imports referenced in controller and domain service files
- [x] 1.2 Verify CropController, DeviceController, EdgeNodeRegistryController, and AwdConfigServiceImpl compilation and linter cleanliness

## 2. Domain & Unit Test Suite

- [x] 2.1 Add unit tests for FieldTopologyServiceImpl and AwdConfigValidator domain rules
- [x] 2.2 Add unit tests for PayloadDecoder and LoRaWAN telemetry ingestion parsing
- [x] 2.3 Add unit tests for IrrigationDecisionServiceImpl verifying autonomous decision rules are recorded without cloud-side autonomous decision generation
- [x] 2.4 Add unit tests for CommandStateMachineServiceImpl and ManualControlServiceImpl verifying state machine transitions and safety limits

## 3. Persistence & Security Integration Tests

- [x] 3.1 Add repository integration tests for entity relationships, foreign keys, unique constraints, and dynamic queries across EdgeNodeRepository, MonitoringZoneRepository, and TelemetryReadingRepository
- [x] 3.2 Add API controller security authorization tests (MockMvc) for ADMIN, OPERATOR, VIEWER, and EDGE_NODE security roles

## 4. Telemetry Boundary & Downlink Sync Tests

- [x] 4.1 Add telemetry ingestion boundary tests covering idempotency, duplicate frame counter filtering, malformed payloads, and stale node health detection
- [x] 4.2 Add configuration versioning and downlink sync tests covering @Version optimistic locking, task creation, downlink queueing, acknowledgement processing, and timeout retry handling
- [x] 4.3 Add emergency-stop priority override tests verifying emergency valve shutoff overrides active manual/scheduled commands

## 5. End-to-End Realtime & Regression Verification

- [x] 5.1 Add end-to-end workflow tests verifying telemetry flow from ingestion through node health updates, zone aggregation, audit logging, and WebSocket event publication
- [x] 5.2 Execute complete backend test suite mvn clean test and verify 100% build pass rate
