# Design: AquaFlow Backend Foundation

## Context

The AquaFlow backend currently has an inconsistent structure that makes it difficult to maintain, scale, and deploy new features. The codebase contains duplicate patterns, scattered across different package levels without clear boundaries between concerns. Existing profile configurations are mixed with application logic, and API endpoints lack consistent versioning and error handling standards. The existing tests work but rely on implicit understanding of the application structure.

The current codebase demonstrates that the application is already functional but lacks architectural consistency. The existing implementation includes:
- Spring Boot REST API with authentication (JWT-based)
- JPA with Flyway for database migrations
- Comprehensive entity model for the AquaFlow domain (Zone, Crop, Schedule, SensorReading, Device, Alert, User)
- MQTT integration for sensor data ingestion
- Rich security configuration with roles and access control
- Global exception handling with structured error responses
- Spring Boot Actuator with OpenAPI documentation

Constraints and stakeholder interests:
- **Product**: Wants consistent deployment across environments (local, test, production) and reliable API contracts
- **Development**: Needs clear structure for onboarding and maintenance
- **Operations**: Requires standard logging and observability with environment-aware configuration
- **Security**: Already robust but needs consistency and auditability

## Goals / Non-Goals

**Goals:**
- Establish a modular, package-oriented architecture with clear separation of concerns
- Define and maintain environment-specific configurations (local/test/production)
- Standardize API contracts under a clear versioned endpoint structure
- Implement consistent error handling and logging patterns across the application
- Define clear service and repository boundaries with defined contracts
- Provide migration path from current structure to foundation with minimal disruption

**Non-Goals:**
- Replace existing functional implementations with new ones
- Redesign domain model or business logic
- Build new features or extend current capabilities beyond foundation requirements
- Replace existing tests with new test frameworks
- Rebuild authentication and security infrastructure (keep existing where possible)

## Decisions

### Package Structure Decision: Modular Architecture over Flat Structure

**Why:** A modular package structure (`config`, `api`, `domain`, `infrastructure`, `scheduler`, `persistence`) provides clear boundaries, better dependency management, and easier onboarding. This enables focused development, testing, and deployment.

**Alternatives Considered:**
- Keep existing structure (but inconsistent)
- Use layered architecture (controller/service/repository) which is too flat
- Use feature-sliced design (complex for a backend)

**Implementation:** Map existing packages to modules: `controller` → `api`, `service` → `domain`, `repository` → `persistence`, `config` and `security` → `infrastructure`, `mqtt` and `web` → `infrastructure`.

### Configuration Profile Decision: Three-Tiered Environment Management

**Why:** Distinct environment profiles (`local`, `test`, `production`) allow for environment-specific tuning while maintaining a single source of truth for application properties.

**Alternatives Considered:**
- Environment variables only (less structured)
- Environment-specific property files mixed in main resources (too complex)
- External configuration management (overkill for current scope)

**Implementation:** Create separate profile property files (`application-local.properties`, `application-test.properties`, `application-prod.properties`) that extend a base `application.properties`.

### API Versioning Decision: URL Path Versioning (/api/v1)

**Why:** URL path versioning is the most discoverable, provides clear documentation, and allows for backward compatibility while being simple to implement and understand.

**Alternatives Considered:**
- HTTP header versioning (less discoverable)
- Media type versioning (complex for internal APIs)
- Query parameter versioning (less RESTful)

**Implementation:** Prefix all controllers with `/api/v1` and establish this pattern for future version increments.

### Error Handling Decision: Global Exception Handler with Structured Responses

**Why:** A global exception handler ensures consistent error responses, reduces duplicate code, and provides a unified error format for all client applications.

**Alternatives Considered:**
- Controller-specific error handling (inconsistent responses)
- Spring Boot default error handling (too generic)

**Implementation:** Maintain existing `GlobalExceptionHandler` with structured `ErrorResponse` format for all API endpoints.

### Logging Decision: Correlation ID + Profile-Based Logging Levels

**Why:** Request correlation IDs enable end-to-end tracing, while profile-based logging ensures appropriate verbosity for each environment.

**Alternatives Considered:**
- Simple logging without correlation IDs (no traceability)
- Centralized logging only (requires infrastructure changes)

**Implementation:** Use `CorrelationIdFilter` and profile-aware logback configuration for structured logging.

### Repository Decision: JPA + Flyway Persistence Pattern

**Why:** The existing JPA/Flyway combination is already working; the foundation should leverage it rather than introduce new patterns.

**Alternatives Considered:**
- Introduce Spring Data MongoDB (different data store)
- Use Hibernate native queries extensively (over-engineering)

**Implementation:** Keep existing JPA repositories with Flyway migrations for schema evolution.

## Risks / Trade-offs

**[Migration Risk]** → Maintain backward compatibility during package reorganization
- Risk: Breaking existing imports and tests
- Mitigation: Update package declarations carefully; ensure all code is migrated with existing functionality intact; run existing tests at each step.

**[Configuration Complexity]** → Too much duplication or insufficient separation in profiles
- Risk: Environment configurations become hard to maintain
- Mitigation: Use inheritance (`@Import`) and environment-specific property files; consolidate common configuration in base profiles; validate with comprehensive test coverage.

**[API Versioning Impact]** → Version prefix affects clients using current endpoints
- Risk: Breaking changes if not handled carefully
- Mitigation: Keep existing endpoint behaviors identical; use versioned structure for future extensibility; document migration paths.

**[Logging Overhead]** → Correlation IDs may add performance overhead
- Risk: Additional processing per request
- Mitigation: Only apply correlation ID to production and debug profiles; use efficient correlation ID generation; make filtering optional.

**[Package Migration Challenges]** → Moving code between packages breaks tooling and IDE configurations
- Risk: Development environment disruption
- Mitigation: Use gradual migration; update Maven/Gradle configurations; test compilation and startup; provide clear upgrade path for IDEs.

## Migration Plan

### Phase 1: Preparation (Week 1)
- Add new package structure alongside existing one
- Create profile-specific property files
- Add logging configuration with correlation IDs
- Update `pom.xml` if needed
- Establish baseline measurements

### Phase 2: Refactoring (Weeks 2-3)
- Migrate configuration and security packages to `infrastructure`
- Move web, mqtt, and other cross-cutting concerns to `infrastructure`
- Migrate `controller` to `api` with `/api/v1` prefix
- Migrate `service` to `domain` with clear interfaces
- Migrate `repository` to `persistence` with Spring Data interfaces
- Update all imports and test configurations

### Phase 3: Validation (Week 4)
- Run existing test suite to verify functionality
- Conduct integration testing with profile-specific configurations
- Perform performance benchmarks
- Validate error handling and logging
- Update documentation and README

### Phase 4: Cleanup (Week 5)
- Remove old package structure
- Consolidate configuration files
- Update CI/CD pipeline references
- Archive migration changes if needed

## Open Questions

- **IDE Integration**: How to handle IDE package reorganization to minimize disruption for developers?
- **Test Migration**: What's the best approach to migrate existing tests to the new package structure?
- **Rollback Strategy**: If issues arise during migration, what's the rollback plan to restore previous functionality?
- **Future Extensibility**: How to structure the new foundation to easily accommodate new modules without architectural debt?
