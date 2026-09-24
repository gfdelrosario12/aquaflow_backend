## 1. Domain Entities, DTOs, and Repositories

- [x] 1.1 Update `IrrigationDecision` entity with rich decision reporting fields (`cropStage`, `confidence`, `actualDurationMinutes`, `failureReason`, `configVersion`, `correlationId`, telemetry inputs)
- [x] 1.2 Create/Update DTOs: `IrrigationDecisionRequest`, `IrrigationDecisionResponse`, `FieldIrrigationStatusResponse`
- [x] 1.3 Update `IrrigationDecisionRepository` with custom query methods (findByFieldId, findByEdgeNodeId, findByExecutionStatus)

## 2. Decision Validation & Service Layer

- [x] 2.1 Implement `IrrigationDecisionValidator` for bound and payload validations
- [x] 2.2 Implement `IrrigationDecisionService` interface and `IrrigationDecisionServiceImpl`
- [x] 2.3 Implement system event publishing (`IRRIGATION_DECISION_MADE`, `IRRIGATION_EXECUTION_UPDATED`) upon decision reporting and status updates
- [x] 2.4 Update `DtoMapper` for decision mapping

## 3. REST Controller Endpoints

- [x] 3.1 Implement `POST /api/v1/irrigation/decisions` endpoint in `IrrigationDecisionController`
- [x] 3.2 Implement `GET /api/v1/irrigation/decisions` endpoint in `IrrigationDecisionController`
- [x] 3.3 Implement `GET /api/v1/irrigation/decisions/{id}` endpoint in `IrrigationDecisionController`
- [x] 3.4 Implement `GET /api/v1/irrigation/field/{fieldId}/status` endpoint in `IrrigationDecisionController`

## 4. Verification and Integration Testing

- [x] 4.1 Write unit tests for `IrrigationDecisionValidator` and `IrrigationDecisionServiceImpl`
- [x] 4.2 Write unit/WebMvc tests for `IrrigationDecisionController`
- [x] 4.3 Run `mvn clean compile` to verify clean compilation
- [x] 4.4 Run `mvn test` to verify all unit and integration tests pass

