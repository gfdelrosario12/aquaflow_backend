## 1. DTO Requests and Response Representations

- [ ] 1.1 Create `FieldRequest` and `FieldResponse` DTOs in `com.aquaflow.backend.dto.request` and `com.aquaflow.backend.dto.response`
- [ ] 1.2 Create `UpdateCropStageRequest` and `UpdateAwdProfileRequest` DTOs with Bean Validation annotations
- [ ] 1.3 Create read-optimized topology DTOs (`FieldTopologyResponse`, `MonitoringZoneTopologyResponse`, `MonitoringPointResponse`)
- [ ] 1.4 Update `DtoMapper` with mapping methods for `FieldTopologyResponse` and nested topology trees
- [x] 1.1 Create `FieldRequest` and `FieldResponse` DTOs in `com.aquaflow.backend.dto.request` and `com.aquaflow.backend.dto.response`
- [x] 1.2 Create `UpdateCropStageRequest` and `UpdateAwdProfileRequest` DTOs with Bean Validation annotations
- [x] 1.3 Create read-optimized topology DTOs (`FieldTopologyResponse`, `MonitoringZoneTopologyResponse`, `MonitoringPointResponse`)
- [x] 1.4 Update `DtoMapper` with mapping methods for `FieldTopologyResponse` and nested topology trees

## 2. Domain Services and Validation Rules

- [ ] 2.1 Create `FieldService` interface and `FieldServiceImpl` implementation in `com.aquaflow.backend.domain`
- [ ] 2.2 Create `ZoneService` interface and `ZoneServiceImpl` implementation in `com.aquaflow.backend.domain`
- [ ] 2.3 Create `FieldTopologyService` for read-optimized field hierarchy fetching
- [ ] 2.4 Implement assignment integrity rules verifying valid node-to-zone and node-to-point assignments in domain services
- [x] 2.1 Create `FieldService` interface and `FieldServiceImpl` implementation in `com.aquaflow.backend.domain`
- [x] 2.2 Create `ZoneService` interface and `ZoneServiceImpl` implementation in `com.aquaflow.backend.domain`
- [x] 2.3 Create `FieldTopologyService` for read-optimized field hierarchy fetching
- [x] 2.4 Implement assignment integrity rules verifying valid node-to-zone and node-to-point assignments in domain services

## 3. REST API Controllers and Endpoint Exposure

- [ ] 3.1 Create `FieldController` in `com.aquaflow.backend.api` with `GET /api/v1/fields`, `GET /api/v1/fields/{id}`, `POST /api/v1/fields`, and `GET /api/v1/fields/{id}/topology`
- [ ] 3.2 Create `ZoneController` in `com.aquaflow.backend.api` with `GET /api/v1/zones/{id}`, `PUT /api/v1/zones/{id}/crop-stage`, and `PUT /api/v1/zones/{id}/awd-profile`
- [ ] 3.3 Ensure controller exception handlers map topology validation errors to HTTP 400/404/409 responses with structured `ErrorResponse`
- [x] 3.1 Create `FieldController` in `com.aquaflow.backend.api` with `GET /api/v1/fields`, `GET /api/v1/fields/{id}`, `POST /api/v1/fields`, and `GET /api/v1/fields/{id}/topology`
- [x] 3.2 Create `ZoneController` in `com.aquaflow.backend.api` with `GET /api/v1/zones/{id}`, `PUT /api/v1/zones/{id}/crop-stage`, and `PUT /api/v1/zones/{id}/awd-profile`
- [x] 3.3 Ensure controller exception handlers map topology validation errors to HTTP 400/404/409 responses with structured `ErrorResponse`

## 4. Verification and Integration Testing

- [ ] 4.1 Write unit tests for `FieldServiceImpl`, `ZoneServiceImpl`, and `FieldTopologyService`
- [ ] 4.2 Write unit/WebMvc tests for `FieldController` and `ZoneController` endpoints
- [ ] 4.3 Run `mvn clean compile` to verify clean compilation
- [ ] 4.4 Run `mvn test` to verify all unit and integration tests pass

- [x] 4.1 Write unit tests for `FieldServiceImpl`, `ZoneServiceImpl`, and `FieldTopologyService`
- [x] 4.2 Write unit/WebMvc tests for `FieldController` and `ZoneController` endpoints
- [x] 4.3 Run `mvn clean compile` to verify clean compilation
- [x] 4.4 Run `mvn test` to verify all unit and integration tests pass
