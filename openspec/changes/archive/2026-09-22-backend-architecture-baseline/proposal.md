## Why

The AquaFlow backend is currently a bare Spring Boot skeleton with no domain logic, no database integration, and no API surface. To support the target autonomous edge irrigation architecture, the backend must provide sensor data ingestion, irrigation scheduling, zone management, crop tracking, and edge-device communication. This proposal documents the baseline state, what can be retained, what must be refactored, and what is missing.

## What Changes

- **BREAKING** Remove the default `@SpringBootApplication`-only skeleton; replace with a fully configured application with domain layer, persistence layer, and REST API
- **BREAKING** Replace empty `application.properties` with full configuration for datasource, JPA, MQTT (edge device communication), actuator, and caching
- **BREAKING** Replace `spring-boot-starter` with `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-integration`, `spring-boot-starter-actuator`, `spring-boot-starter-security`, `spring-boot-starter-validation`, `flyway`, and `mqtt` dependencies
- New: Entity/domain layer for Irrigation, Zone, Crop, Sensor, Device, Schedule, and Alert
- New: Repository layer with Spring Data JPA repositories
- New: Service layer for irrigation scheduling, sensor data processing, device management, and alerting
- New: REST controller layer with versioned API endpoints (`/api/v1/`) for all domain resources
- New: Global exception handling with `@ControllerAdvice` and standardized error responses
- New: Database migration scripts via Flyway (H2 for dev/test, PostgreSQL for prod)
- New: MQTT integration for edge device communication (sensors and actuators)
- New: Spring Security with JWT authentication and role-based authorization
- New: OpenAPI/Swagger documentation
- New: DTO layer separating API contracts from entity models
- New: Validation annotations on all incoming request DTOs
- New: Dockerfile and docker-compose for local development
- New: Comprehensive test suite (unit, integration, and repository tests)
- New: Logging configuration (Logback) with structured output

## Capabilities

### New Capabilities
- `edge-irrigation-core`: Central irrigation logic including scheduling, zone management, crop tracking, and water usage optimization
- `sensor-data-ingestion`: MQTT-based sensor data ingestion from edge devices with validation and persistence
- `device-management`: Edge device registration, status monitoring, heartbeat tracking, and lifecycle management
- `irrigation-api`: Versioned REST API for all irrigation operations (CRUD, scheduling, reporting)
- `auth-security`: JWT-based authentication and role-based authorization for API access
- `data-persistence`: JPA entity definitions, repository layer, Flyway migrations, and multi-profile datasource configuration
- `error-handling`: Global exception handling with standardized error response format
- `observability`: Spring Boot Actuator health checks, metrics, OpenAPI documentation, and structured logging

### Modified Capabilities

*(None - no existing specs to modify)*

## Impact

- **Code**: Entire backend layer will be restructured; `BackendApplication.java` remains as entry point but all other code is new
- **APIs**: No existing APIs to break (none exist currently); new `/api/v1/**` endpoints introduced
- **Dependencies**: Major POM changes - new starters for web, JPA, security, validation, integration, actuator, Flyway, MQTT
- **Database**: New schema required; Flyway migrations needed (H2 dev, PostgreSQL prod)
- **Infrastructure**: Dockerfile and docker-compose required for local development; MQTT broker (Mosquitto) needed for edge device testing
- **Tests**: All tests are new; existing `BackendApplicationTests.java` context load test can be retained
