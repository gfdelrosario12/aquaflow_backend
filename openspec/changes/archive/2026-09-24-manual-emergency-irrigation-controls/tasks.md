## 1. Domain Models & DTOs

- [ ] 1.1 Create DTO classes: `ManualStartRequest`, `ManualStopRequest`, `EmergencyStopRequest`, and `CommandStatusResponse`
- [ ] 1.2 Enforce validation constraints on request DTOs (operator ID, field ID, rationale, duration bounds)
- [x] 1.1 Create DTO classes: `ManualStartRequest`, `ManualStopRequest`, `EmergencyStopRequest`, and `CommandStatusResponse`
- [x] 1.2 Enforce validation constraints on request DTOs (operator ID, field ID, rationale, duration bounds)

## 2. Service Layer Implementation

- [ ] 2.1 Implement `ManualControlService` to process manual start/stop actions, generate audit records, and queue downlink messages
- [ ] 2.2 Implement `EmergencyStopService` to process emergency stop commands with highest priority dispatch, raise audit logs, and publish system events
- [ ] 2.3 Implement command status lookup and lifecycle tracking handling (`QUEUED`, `DELIVERED`, `ACKNOWLEDGED`, `EXECUTED`, `REJECTED_SAFETY_INTERLOCK`, `FAILED`)
- [x] 2.1 Implement `ManualControlService` to process manual start/stop actions, generate audit records, and queue downlink messages
- [x] 2.2 Implement `EmergencyStopService` to process emergency stop commands with highest priority dispatch, raise audit logs, and publish system events
- [x] 2.3 Implement command status lookup and lifecycle tracking handling (`QUEUED`, `DELIVERED`, `ACKNOWLEDGED`, `EXECUTED`, `REJECTED_SAFETY_INTERLOCK`, `FAILED`)

## 3. REST API Endpoints & Controller

- [ ] 3.1 Expose `POST /api/v1/irrigation/manual/start` in `IrrigationController`
- [ ] 3.2 Expose `POST /api/v1/irrigation/manual/stop` in `IrrigationController`
- [ ] 3.3 Expose `POST /api/v1/irrigation/emergency-stop` in `IrrigationController`
- [x] 3.1 Expose `POST /api/v1/irrigation/manual/start` in `IrrigationController`
- [x] 3.2 Expose `POST /api/v1/irrigation/manual/stop` in `IrrigationController`
- [x] 3.3 Expose `POST /api/v1/irrigation/emergency-stop` in `IrrigationController`

## 4. Verification & Testing

- [ ] 4.1 Create unit tests for `ManualControlService` and `EmergencyStopService`
- [ ] 4.2 Create integration/controller tests for `IrrigationController` manual and emergency endpoints
- [ ] 4.3 Verify full project compilation and execute all tests using `mvn clean compile` and `mvn test`
- [x] 4.1 Create unit tests for `ManualControlService` and `EmergencyStopService`
- [x] 4.2 Create integration/controller tests for `IrrigationController` manual and emergency endpoints
- [x] 4.3 Verify full project compilation and execute all tests using `mvn clean compile` and `mvn test`

