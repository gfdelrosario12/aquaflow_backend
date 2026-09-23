## Context

The AquaFlow backend is a minimal Spring Boot 4.1.1 skeleton (Java 17, Maven) with no domain logic, no persistence, and no API surface. The target is an autonomous edge irrigation system requiring sensor data ingestion, irrigation scheduling, zone/crop management, device communication via MQTT, JWT authentication, and Docker-based local development. See proposal.md for motivation.

## Goals / Non-Goals

**Goals:**
- Establish a fully functional Spring Boot backend with domain-driven architecture
- Enable MQTT-based edge device communication for real-time sensor data
- Provide versioned REST API for all irrigation operations
- Support multi-profile deployment (H2 dev, PostgreSQL prod)
- Enable containerized local development with Docker

**Non-Goals:**
- Implement frontend/UI integration (out of scope for backend-only change)
- Implement actual over-the-air firmware update delivery (OTA is target-only in this phase)
- Implement third-party weather API integration (placeholder for future phase)
- Implement multi-tenancy (single-tenant deployment assumed)

## Decisions

### Decision 1: Layered Architecture (Controller → Service → Repository → Entity)
- **Choice**: Standard Spring Boot layered architecture with DTOs separating API from domain
- **Rationale**: Clear separation of concerns; matches team familiarity; easy to test each layer independently
- **Alternatives**: Hexagonal architecture (rejected - overkill for initial phase), Serverless (rejected - not suited for persistent irrigation scheduling)

### Decision 2: MQTT for Edge Communication
- **Choice**: Spring Integration with Eclipse Paho MQTT client
- **Rationale**: IoT standard protocol; lightweight for edge devices; Spring Integration provides natural fit for message-driven architecture
- **Alternatives**: HTTP polling (rejected - too resource-intensive for edge devices), AMQP (rejected - heavier protocol than needed)

### Decision 3: Flyway for Database Migrations
- **Choice**: Flyway with SQL-based migrations
- **Rationale**: Simple, version-controlled schema evolution; SQL is portable and transparent; H2/PostgreSQL compatibility via standard SQL
- **Alternatives**: Liquibase (rejected - XML/YAML adds indirection), JPA auto-ddl (rejected - not suitable for production)

### Decision 4: JWT with Spring Security
- **Choice**: Stateless JWT with BCrypt password hashing, role-based access
- **Rationale**: Stateless scaling; no session store needed; standard for REST APIs
- **Alternatives**: Session-based auth (rejected - harder to scale), OAuth2/OIDC (rejected - overkill for initial deployment)

### Decision 5: PostgreSQL as Production Database
- **Choice**: PostgreSQL with H2 for dev/test
- **Rationale**: Robust relational DB with JSON support for sensor data; H2 enables fast local development without external dependencies
- **Alternatives**: MongoDB (rejected - relational structure fits better), MySQL (rejected - team standardized on PostgreSQL)

## Risks / Trade-offs

- **Risk**: MQTT broker dependency for core functionality → **Mitigation**: Use resilient client with automatic reconnection; sensor data buffered during disconnect
- **Risk**: Spring Boot 4.1.1 is recent with potential ecosystem lag → **Mitigation**: Verify all starter compatibility; pin versions where needed
- **Trade-off**: H2 in-memory loses data on restart → **Mitigation**: Only used in dev/test; prod uses PostgreSQL; migration scripts tested on both
- **Risk**: Scope creep from edge device complexity → **Mitigation**: Strict MVP scope per specs; firmware OTA and weather APIs deferred

## Migration Plan

1. Apply POM dependency changes (add web, JPA, security, integration, actuator, validation, Flyway, MQTT starters)
2. Create entity and repository layer with Flyway migrations
3. Implement service and controller layer per specs
4. Configure MQTT integration and edge device communication
5. Add Dockerfile and docker-compose.yml
6. Update application.properties with multi-profile configuration
7. Add test suite and run full verification

## Open Questions

1. **MQTT broker deployment**: Will Mosquitto be containerized in docker-compose or run as external service? (Decide before Docker tasks)
2. **Sensor data retention policy**: How long should raw sensor readings be retained before archival? (Decide before data-persistence implementation)
