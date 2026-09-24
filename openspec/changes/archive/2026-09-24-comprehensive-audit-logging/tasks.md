## 1. Domain Models & Persistence Layer

- [ ] 1.1 Create `SystemAuditLog` JPA entity with fields (`eventType`, `actor`, `entityType`, `entityId`, `correlationId`, `previousState`, `resultingState`, `payloadJson`, `createdAt`)
- [ ] 1.2 Create `SystemAuditLogRepository` supporting queries by entity, correlation ID, date range, and pagination
- [ ] 1.3 Update/Verify `IrrigationAuditLog` and `IrrigationAuditLogRepository` to support querying by correlation ID and event type
- [x] 1.1 Create `SystemAuditLog` JPA entity with fields (`eventType`, `actor`, `entityType`, `entityId`, `correlationId`, `previousState`, `resultingState`, `payloadJson`, `createdAt`)
- [x] 1.2 Create `SystemAuditLogRepository` supporting queries by entity, correlation ID, date range, and pagination
- [x] 1.3 Update/Verify `IrrigationAuditLog` and `IrrigationAuditLogRepository` to support querying by correlation ID and event type

## 2. DTOs & Service Layer

- [ ] 2.1 Create DTOs: `SystemAuditLogResponse`, `AuditQueryRequest`, `AuditExportResponse`
- [ ] 2.2 Create `AuditLogService` interface and implementation `AuditLogServiceImpl` for writing, querying, and exporting system and irrigation audit records in CSV/JSON formats
- [ ] 2.3 Connect `SystemEventListener` to automatically record audit log entries on `SystemEvent` occurrences (node status changes, alarms, emergency stops, config syncs)
- [x] 2.1 Create DTOs: `SystemAuditLogResponse`, `AuditQueryRequest`, `AuditExportResponse`
- [x] 2.2 Create `AuditLogService` interface and implementation `AuditLogServiceImpl` for writing, querying, and exporting system and irrigation audit records in CSV/JSON formats
- [x] 2.3 Connect `SystemEventListener` to automatically record audit log entries on `SystemEvent` occurrences (node status changes, alarms, emergency stops, config syncs)

## 3. REST API Endpoints

- [ ] 3.1 Create `AuditLogController` exposing `GET /api/v1/audit/irrigation` for querying irrigation audit logs
- [ ] 3.2 Expose `GET /api/v1/audit/system` in `AuditLogController` for querying system audit logs
- [ ] 3.3 Expose `GET /api/v1/audit/export` in `AuditLogController` for exporting filtered audit logs in CSV/JSON formats
- [x] 3.1 Create `AuditLogController` exposing `GET /api/v1/audit/irrigation` for querying irrigation audit logs
- [x] 3.2 Expose `GET /api/v1/audit/system` in `AuditLogController` for querying system audit logs
- [x] 3.3 Expose `GET /api/v1/audit/export` in `AuditLogController` for exporting filtered audit logs in CSV/JSON formats

## 4. Verification & Testing

- [ ] 4.1 Create unit tests for `AuditLogServiceImpl` (querying, event logging, CSV/JSON formatting)
- [ ] 4.2 Create controller tests for `AuditLogController` query and export endpoints
- [ ] 4.3 Verify full project compilation and execute all tests using `mvn clean compile` and `mvn test`
- [x] 4.1 Create unit tests for `AuditLogServiceImpl` (querying, event logging, CSV/JSON formatting)
- [x] 4.2 Create controller tests for `AuditLogController` query and export endpoints
- [x] 4.3 Verify full project compilation and execute all tests using `mvn clean compile` and `mvn test`

