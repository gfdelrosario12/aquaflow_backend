# Proposal: AquaFlow Backend Foundation

## Why

The AquaFlow backend is growing rapidly with new domain features, but the codebase lacks a consistent modular structure, environment-specific configuration, and shared API conventions. Without a solid foundation, each new feature risks introducing inconsistent patterns, making the system harder to maintain, test, and deploy across local, test, and production environments.

This change establishes the architectural baseline needed to support the full AquaFlow domain implementation while preserving existing authentication and API behavior.

## What Changes

- **New modular package structure**: Reorganize the codebase into `config`, `api`, `domain`, `infrastructure`, `scheduler`, and `persistence` packages to enforce clear separation of concerns.
- **Multi-profile configuration**: Establish distinct configuration profiles for `local`, `test`, and `production` environments with environment-specific datasource, security, and logging settings.
- **Consistent API versioning**: Standardize all REST endpoints under `/api/v1` with uniform request/response conventions.
- **Validation and JSON serialization**: Apply consistent Bean Validation and JSON serialization/deserialization rules across all API contracts.
- **Structured error responses**: Define a single error response format and global exception handling strategy across the API.
- **Request correlation and logging**: Introduce correlation IDs for request tracing and establish structured logging conventions per profile.
- **Service/repository boundaries**: Define clear contracts between API controllers, domain services, and persistence repositories.
- **PostgreSQL and Flyway foundation**: Add the base configuration and migration framework required for PostgreSQL persistence without implementing the full domain.

## Capabilities

### New Capabilities

- `modular-package-structure`: Organizes the codebase into `config`, `api`, `domain`, `infrastructure`, `scheduler`, and `persistence` packages with clear responsibilities and dependency direction.
- `multi-profile-configuration`: Provides `local`, `test`, and `production` Spring profiles with environment-specific datasource, security, logging, and external service settings.

### Modified Capabilities

- `irrigation-api`: Standardizes API versioning under `/api/v1` and establishes uniform request/response conventions.
- `error-handling`: Introduces a single structured error response format and global exception handling across all endpoints.
- `observability`: Adds request correlation IDs and structured logging conventions for tracing and debugging.
- `data-persistence`: Establishes the PostgreSQL datasource and Flyway migration foundation required for domain persistence.

## Impact

- **Code**: Reorganization of existing packages (`controller`, `dto`, `entity`, `service`, `repository`, `config`, `security`, `mqtt`, `exception`, `util`, `web`) into the new modular structure.
- **APIs**: All REST endpoints migrated to `/api/v1` versioning; existing endpoint paths and authentication behavior preserved where possible.
- **Configuration**: New profile-specific property files (`application-local.properties`, `application-test.properties`, `application-prod.properties`) replacing or supplementing existing profile configuration.
- **Dependencies**: Flyway and PostgreSQL drivers already present in `pom.xml`; no new major dependency additions required beyond what is already established.
- **Tests**: Existing unit and integration tests updated to reflect the new package structure and profile configuration.
