## 1. DTO Requests and Response Objects

- [ ] 1.1 Create `RegisterNodeRequest`, `CommissionNodeRequest`, and `CommissionNodeResponse` DTOs in `com.aquaflow.backend.dto`
- [ ] 1.2 Create `ReplaceNodeRequest`, `UpdateTransmissionRequest`, and `NodeHealthResponse` DTOs with validation rules
- [ ] 1.3 Ensure `EdgeNodeResponse` and mapper hide raw secrets and include lifecycle, health, and transmission metadata
- [x] 1.1 Create `RegisterNodeRequest`, `CommissionNodeRequest`, and `CommissionNodeResponse` DTOs in `com.aquaflow.backend.dto`
- [x] 1.2 Create `ReplaceNodeRequest`, `UpdateTransmissionRequest`, and `NodeHealthResponse` DTOs with validation rules
- [x] 1.3 Ensure `EdgeNodeResponse` and mapper hide raw secrets and include lifecycle, health, and transmission metadata

## 2. Domain Services and Lifecycle Invariants

- [ ] 2.1 Create `EdgeNodeRegistryService` interface in `com.aquaflow.backend.domain`
- [ ] 2.2 Implement `EdgeNodeRegistryServiceImpl` with registration, commissioning, decommissioning, and replacement logic
- [ ] 2.3 Implement transmission configuration and health/telemetry query methods in `EdgeNodeRegistryServiceImpl`
- [ ] 2.4 Enforce node lifecycle operational invariants prohibiting inactive/decommissioned nodes from telemetry and irrigation sync
- [x] 2.1 Create `EdgeNodeRegistryService` interface in `com.aquaflow.backend.domain`
- [x] 2.2 Implement `EdgeNodeRegistryServiceImpl` with registration, commissioning, decommissioning, and replacement logic
- [x] 2.3 Implement transmission configuration and health/telemetry query methods in `EdgeNodeRegistryServiceImpl`
- [x] 2.4 Enforce node lifecycle operational invariants prohibiting inactive/decommissioned nodes from telemetry and irrigation sync

## 3. REST API Controllers and Endpoint Exposure

- [ ] 3.1 Create `EdgeNodeRegistryController` in `com.aquaflow.backend.api` mapped to `/api/v1/nodes`
- [ ] 3.2 Implement standard CRUD endpoints (`POST /api/v1/nodes`, `GET /api/v1/nodes`, `GET /api/v1/nodes/{id}`, `PUT /api/v1/nodes/{id}`)
- [ ] 3.3 Implement lifecycle action endpoints (`POST /api/v1/nodes/{id}/commission`, `POST /api/v1/nodes/{id}/decommission`, `POST /api/v1/nodes/{id}/replace`)
- [ ] 3.4 Implement transmission, health, and telemetry endpoints (`PUT /api/v1/nodes/{id}/transmission`, `GET /api/v1/nodes/{id}/health`, `GET /api/v1/nodes/{id}/telemetry/latest`, `GET /api/v1/nodes/{id}/telemetry/history`)
- [x] 3.1 Create `EdgeNodeRegistryController` in `com.aquaflow.backend.api` mapped to `/api/v1/nodes`
- [x] 3.2 Implement standard CRUD endpoints (`POST /api/v1/nodes`, `GET /api/v1/nodes`, `GET /api/v1/nodes/{id}`, `PUT /api/v1/nodes/{id}`)
- [x] 3.3 Implement lifecycle action endpoints (`POST /api/v1/nodes/{id}/commission`, `POST /api/v1/nodes/{id}/decommission`, `POST /api/v1/nodes/{id}/replace`)
- [x] 3.4 Implement transmission, health, and telemetry endpoints (`PUT /api/v1/nodes/{id}/transmission`, `GET /api/v1/nodes/{id}/health`, `GET /api/v1/nodes/{id}/telemetry/latest`, `GET /api/v1/nodes/{id}/telemetry/history`)

## 4. Verification and Integration Testing

- [ ] 4.1 Write unit tests for `EdgeNodeRegistryServiceImpl` covering lifecycle state transitions and replacement workflows
- [ ] 4.2 Write unit/WebMvc tests for `EdgeNodeRegistryController` endpoints
- [ ] 4.3 Run `mvn clean compile` to verify clean compilation
- [ ] 4.4 Run `mvn test` to verify all unit and integration tests pass

- [x] 4.1 Write unit tests for `EdgeNodeRegistryServiceImpl` covering lifecycle state transitions and replacement workflows
- [x] 4.2 Write unit/WebMvc tests for `EdgeNodeRegistryController` endpoints
- [x] 4.3 Run `mvn clean compile` to verify clean compilation
- [x] 4.4 Run `mvn test` to verify all unit and integration tests pass
