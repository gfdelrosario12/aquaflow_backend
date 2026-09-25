# deployment/containerization Specification

## Purpose
Provides containerized runtime environment definitions, environment variable bindings, graceful shutdown lifecycle management, and local multi-container orchestration.
## Requirements
### Requirement: System SHALL provide a production multi-stage Docker container build
The system SHALL provide a multi-stage `Dockerfile` compiling the Spring Boot application using Java 17, running under a dedicated non-root user, and leveraging layer caching for dependencies.

#### Scenario: Production Docker build and execution
- **WHEN** the container image is built and started in production mode
- **THEN** application runs under a non-root user with optimized JVM memory flags and unprivileged file access

### Requirement: System SHALL support environment-based configuration without embedded credentials
The system SHALL externalize all infrastructure endpoints, credentials, JWT secrets, and database connection settings via environment variables with standard fallback defaults for dev/local execution.

#### Scenario: Custom environment configuration override
- **WHEN** environment variables `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `REDIS_HOST`, and `MQTT_BROKER_URL` are set
- **THEN** backend connects to the specified external infrastructure endpoints without using default fallbacks

### Requirement: System SHALL enforce graceful shutdown on SIGTERM
The system SHALL handle `SIGTERM` signals by stopping acceptance of new requests, completing active in-flight requests, and cleanly shutting down database and MQTT connections within the configured grace period.

#### Scenario: Graceful container termination
- **WHEN** container receives a `SIGTERM` signal
- **THEN** HTTP server finishes processing active requests before process termination

### Requirement: System SHALL provide local multi-container development environment via Docker Compose
The system SHALL provide a `docker-compose.yml` orchestrating PostgreSQL 15, Redis 7, Eclipse Mosquitto MQTT broker, and the Spring Boot application container with health-check dependency ordering.

#### Scenario: Local stack startup via Docker Compose
- **WHEN** `docker compose up` is executed
- **THEN** PostgreSQL, Redis, Mosquitto, and the Spring Boot backend container start in proper dependency order and achieve healthy status

