# Tasks: openapi-documentation

- [x] 1. Add springdoc-openapi dependencies and OpenApiConfig configuration
  - [x] 1.1 Update `pom.xml` with `springdoc-openapi-starter-webmvc-ui` (v2.8.5)
  - [x] 1.2 Create `OpenApiConfig.java` bean definition with metadata and bearerAuth security scheme
- [x] 2. Annotate API Controllers and DTO Request/Response Schemas
  - [x] 2.1 Annotate Auth, Crop, Field, and Zone controllers
  - [x] 2.2 Annotate EdgeNodeRegistry, TelemetryIngestion, and AWDConfig controllers
  - [x] 2.3 Annotate IrrigationSchedule, Irrigation, CommandStateMachine, AuditLog, and Device controllers
  - [x] 2.4 Add `@Schema` annotations to DTO models
- [x] 3. Verification and Integration Testing
  - [x] 3.1 Create and verify `OpenApiIntegrationTest.java` for `/v3/api-docs` endpoint
  - [x] 3.2 Run complete test suite `mvn clean test` to ensure 100% pass rate
