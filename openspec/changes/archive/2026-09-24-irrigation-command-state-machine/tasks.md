## 1. Domain Models & Repositories

- [x] 1.1 Create `CommandState` enum with all 9 lifecycle states (`ACCEPTED`, `QUEUED`, `DOWNLINK_TRANSMITTED`, `EDGE_ACKNOWLEDGED`, `EXECUTING`, `COMPLETED`, `FAILED`, `CANCELLED`, `OVERRIDDEN`)
- [x] 1.2 Create `IrrigationCommandRecord` entity and `CommandStateTransitionHistory` entity
- [x] 1.3 Create Spring Data JPA repositories `IrrigationCommandRecordRepository` and `CommandStateTransitionHistoryRepository`
- [x] 1.4 Create DTOs `CommandStateTransitionRequest`, `CommandStateResponse`, and `CommandTransitionHistoryResponse`

## 2. State Machine Engine & Service Layer

- [x] 2.1 Implement `IrrigationCommandStateMachine` service to validate state transitions and manage transition matrix logic
- [x] 2.2 Implement transition history persistence with timestamps, correlation ID, actor/source, and metadata
- [x] 2.3 Integrate state machine into `ManualControlServiceImpl`, `EmergencyStopServiceImpl`, and `AutonomousDecisionServiceImpl`
- [x] 2.4 Implement emergency stop clearing authorization check preventing unauthorized state resolution

## 3. REST API Endpoints

- [x] 3.1 Create `CommandStateMachineController` (or extend `IrrigationController`) exposing `GET /api/v1/irrigation/commands/{commandId}`, `GET /api/v1/irrigation/commands/{commandId}/history`, and `GET /api/v1/irrigation/commands/field/{fieldId}`
- [x] 3.2 Add transition request endpoint `POST /api/v1/irrigation/commands/{commandId}/transition` for edge events or manual status updates

## 4. Verification & Testing

- [x] 4.1 Write unit tests for `IrrigationCommandStateMachine` testing all valid transitions and rejecting invalid transition attempts (e.g. unstarted completion, completed restarting, unauthorized emergency clearance)
- [x] 4.2 Write integration/controller tests for command state REST endpoints
- [x] 4.3 Verify compilation and execute complete test suite using `mvn test`
