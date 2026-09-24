## 1. Entities, Enums, DTOs, and Repository Setup

- [x] 1.1 Create `CropGrowthStage` enum (`VEGETATIVE`, `REPRODUCTIVE`, `RIPENING`, `FALLOW`)
- [x] 1.2 Create `AwdThresholdConfig` and `AutoIrrigationConfig` JPA entities
- [x] 1.3 Create `AutoIrrigationConfigRequest`, `AutoIrrigationConfigResponse`, and `AwdThresholdConfigRequest` DTOs
- [x] 1.4 Create `AutoIrrigationConfigRepository` and `AwdThresholdConfigRepository`

## 2. Service Layer & Policy Validation Engine

- [x] 2.1 Implement `AwdConfigValidator` checking bounds for operating hours, durations, confidence thresholds, and moisture limits
- [x] 2.2 Implement `AwdConfigService` interface and `AwdConfigServiceImpl` for retrieving and updating field AWD policy
- [x] 2.3 Implement configuration versioning (`configVersion++`), audit tracking (`updatedBy`, `changeReason`), and `CONFIG_SYNCED` event publishing

## 3. REST Controller Endpoints

- [x] 3.1 Implement `GET /api/v1/fields/{fieldId}/awd-config` endpoint in `AwdConfigController`
- [x] 3.2 Implement `PUT /api/v1/fields/{fieldId}/awd-config` endpoint in `AwdConfigController`
- [x] 3.3 Enrich `GET /api/v1/fields/{id}/topology` with effective policy `activeConfigVersion`

## 4. Verification and Integration Testing

- [x] 4.1 Write unit tests for `AwdConfigValidator` and `AwdConfigServiceImpl`
- [x] 4.2 Write unit/WebMvc tests for `AwdConfigController` verifying GET/PUT requests and validation error responses
- [x] 4.3 Run `mvn clean compile` to verify clean compilation
- [x] 4.4 Run `mvn test` to verify all unit and integration tests pass
