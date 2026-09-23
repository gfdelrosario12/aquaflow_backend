## 1. Project Setup

- [x] 1.1 Update pom.xml with required dependencies (web, JPA, security, validation, integration, actuator, Flyway, MQTT, lombok)
- [x] 1.2 Create multi-profile application.properties (application-dev.properties, application-test.properties, application-prod.properties)
- [x] 1.3 Configure logging (logback-spring.xml) with structured JSON output and per-profile levels
- [x] 1.4 Add Dockerfile and docker-compose.yml (app + Mosquitto MQTT broker)

## 2. Data Persistence Layer

- [x] 2.1 Create JPA entities (Zone, Crop, IrrigationSchedule, SensorReading, Device, Alert, User, Schedule)
- [x] 2.2 Create Spring Data JPA repositories for all entities
- [x] 2.3 Create Flyway migration scripts (V1__init_schema.sql) for H2 and PostgreSQL
- [x] 2.4 Configure datasource URLs and credentials per profile

## 3. Domain Models and DTOs

- [x] 3.1 Create request DTOs for all API endpoints (zone, schedule, device, sensor, user)
- [x] 3.2 Create response DTOs for all API endpoints
- [x] 3.3 Add Bean Validation annotations (@NotNull, @Size, @Pattern, @Min, @Max) to request DTOs
- [x] 3.4 Create DTO-to-Entity mapping utilities (manual or MapStruct)

## 4. Auth and Security

- [x] 4.1 Configure Spring Security with JWT filter chain
- [x] 4.2 Implement JWT token generation and validation utilities
- [x] 4.3 Implement user registration and login endpoints
- [x] 4.4 Configure BCrypt password encoding
- [x] 4.5 Define role hierarchy (ADMIN, OPERATOR, VIEWER) and URL-based access rules

## 5. Error Handling

- [x] 5.1 Create standardized error response class (timestamp, status, errorCode, message, details)
- [x] 5.2 Implement GlobalExceptionHandler with @ControllerAdvice
- [x] 5.3 Create specific exception classes (ResourceNotFoundException, ValidationException, UnauthorizedException, ForbiddenException)
- [x] 5.4 Add validation error handler for @Valid annotated request bodies

## 6. Service Layer

- [x] 6.1 Implement ZoneService (CRUD, validation, water capacity checks)
- [x] 6.2 Implement IrrigationScheduleService (create, list, optimize scheduling)
- [x] 6.3 Implement CropService (CRUD for crop types and water requirements)
- [x] 6.4 Implement DeviceService (register, heartbeat, status, firmware)
- [x] 6.5 Implement SensorDataService (ingest, validate, query sensor readings)
- [x] 6.6 Implement AlertService (threshold monitoring, alert generation)
- [x] 6.7 Implement UserService (registration, profile management)

## 7. MQTT Integration

- [x] 7.1 Configure MQTT client connection (broker URL, topic subscriptions)
- [x] 7.2 Implement MQTT message listener for sensor data topics
- [x] 7.3 Implement MQTT publish service for actuator commands
- [x] 7.4 Add reconnection and error handling for MQTT client
- [x] 7.5 Map MQTT messages to sensor readings via SensorDataService

## 8. REST API Controllers

- [x] 8.1 Create ZoneController (CRUD endpoints at /api/v1/zones)
- [x] 8.2 Create IrrigationScheduleController (CRUD at /api/v1/schedules)
- [x] 8.3 Create CropController (CRUD at /api/v1/crops)
- [x] 8.4 Create DeviceController (CRUD at /api/v1/devices)
- [x] 8.5 Create SensorDataController (query at /api/v1/sensors/data)
- [x] 8.6 Create AlertController (list/acknowledge at /api/v1/alerts)
- [x] 8.7 Create AuthController (login/register/refresh at /api/v1/auth)
- [x] 8.8 Add pagination and sorting to all list endpoints

## 9. Observability

- [x] 9.1 Enable Spring Boot Actuator endpoints (health, info, prometheus, metrics)
- [x] 9.2 Configure OpenAPI 3.0 (springdoc-openapi) with API info
- [x] 9.3 Verify actuator security (health open, metrics/prometheus restricted)
- [x] 9.4 Add correlation ID filter for request tracing in logs

## 10. Tests

- [x] 10.1 Write repository tests for all JPA repositories (@DataJpaTest)
- [x] 10.2 Write service layer unit tests (@MockitoTest)
- [x] 10.3 Write controller integration tests (@WebMvcTest)
- [x] 10.4 Write MQTT integration tests
- [x] 10.5 Write security tests (auth, authorization)
- [x] 10.6 Verify all tests pass with `mvn test`