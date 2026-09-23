## 1. Package Structure Refactoring

- [ ] 1.1 Reorder existing packages into modular structure (api, domain, infrastructure, persistence, scheduler, config)
- [ ] 1.2 Move controllers from `controller` package to `api` package
- [ ] 1.3 Move services from `service` package to `domain` package
- [ ] 1.4 Move repositories from `repository` package to `persistence` package
- [ ] 1.5 Move configuration classes to `config` package
- [ ] 1.6 Move security classes to `infrastructure/security` package
- [ ] 1.7 Move MQTT classes to `infrastructure/mqtt` package
- [ ] 1.8 Move exception classes to `infrastructure/exception` package
- [ ] 1.9 Move utility classes to `infrastructure/util` package
- [ ] 1.10 Move web filters to `infrastructure/web` package
- [x] 1.1 Reorder existing packages into modular structure (api, domain, infrastructure, persistence, scheduler, config)
- [x] 1.2 Move controllers from `controller` package to `api` package
- [x] 1.3 Move services from `service` package to `domain` package
- [x] 1.4 Move repositories from `repository` package to `persistence` package
- [x] 1.5 Move configuration classes to `config` package
- [x] 1.6 Move security classes to `infrastructure/security` package
- [x] 1.7 Move MQTT classes to `infrastructure/mqtt` package
- [x] 1.8 Move exception classes to `infrastructure/exception` package
- [x] 1.9 Move utility classes to `infrastructure/util` package
- [x] 1.10 Move web filters to `infrastructure/web` package

## 2. Configuration Profiles

- [ ] 2.1 Create `application-local.properties` with H2 datasource and debug logging
- [ ] 2.2 Create `application-test.properties` with test datasource and info logging
- [ ] 2.3 Create `application-prod.properties` with PostgreSQL datasource and warn logging
- [ ] 2.4 Update `application.properties` with base configuration and profile activation
- [ ] 2.5 Configure logback profiles for environment-specific logging levels
- [x] 2.1 Create `application-local.properties` with H2 datasource and debug logging
- [x] 2.2 Create `application-test.properties` with test datasource and info logging
- [x] 2.3 Create `application-prod.properties` with PostgreSQL datasource and warn logging
- [x] 2.4 Update `application.properties` with base configuration and profile activation
- [x] 2.5 Configure logback profiles for environment-specific logging levels

## 3. API Versioning

- [ ] 3.1 Update all `@RequestMapping` annotations to include `/api/v1` prefix
- [ ] 3.2 Update controller class-level `@RequestMapping` for versioned path
- [ ] 3.3 Verify all endpoints use `/api/v1` prefix
- [ ] 3.4 Update OpenAPI configuration for versioned API documentation
- [x] 3.1 Update all `@RequestMapping` annotations to include `/api/v1` prefix
- [x] 3.2 Update controller class-level `@RequestMapping` for versioned path
- [x] 3.3 Verify all endpoints use `/api/v1` prefix
- [x] 3.4 Update OpenAPI configuration for versioned API documentation

## 4. Error Handling

- [ ] 4.1 Create `ErrorResponse` DTO with field: timestamp, status, errorCode, message, details
- [ ] 4.2 Update `GlobalExceptionHandler` to use structured error response
- [ ] 4.3 Add method-level error handlers for specific exception types
- [ ] 4.4 Include correlation ID in all error responses
- [ ] 4.5 Add field-level validation error detail handling
- [x] 4.1 Create `ErrorResponse` DTO with field: timestamp, status, errorCode, message, details
- [x] 4.2 Update `GlobalExceptionHandler` to use structured error response
- [x] 4.3 Add method-level error handlers for specific exception types
- [x] 4.4 Include correlation ID in all error responses
- [x] 4.5 Add field-level validation error detail handling

## 5. Logging and Correlation

- [ ] 5.1 Create `CorrelationIdFilter` for request correlation
- [ ] 5.2 Create utility for generating correlation IDs
- [ ] 5.3 Configure logback for JSON structured logging output
- [ ] 5.4 Add correlation ID to all log entries via MDC
- [ ] 5.5 Verify logging includes correlation ID in all profile modes
- [x] 5.1 Create `CorrelationIdFilter` for request correlation
- [x] 5.2 Create utility for generating correlation IDs
- [x] 5.3 Configure logback for JSON structured logging output
- [x] 5.4 Add correlation ID to all log entries via MDC
- [x] 5.5 Verify logging includes correlation ID in all profile modes

## 6. Data Persistence

- [ ] 6.1 Create Flyway migration script V1__init_schema.sql for PostgreSQL
- [ ] 6.2 Review entity mappings for H2/PostgreSQL compatibility
- [ ] 6.3 Configure datasource URLs and credentials per profile
- [ ] 6.4 Test Flyway migrations against H2 and PostgreSQL
- [ ] 6.5 Add connection pool configuration per profile
- [x] 6.1 Create Flyway migration script V1__init_schema.sql for PostgreSQL
- [x] 6.2 Review entity mappings for H2/PostgreSQL compatibility
- [x] 6.3 Configure datasource URLs and credentials per profile
- [x] 6.4 Test Flyway migrations against H2 and PostgreSQL
- [x] 6.5 Add connection pool configuration per profile

## 7. Update Existing Tests

- [ ] 7.1 Update test imports for new package structure
- [ ] 7.2 Update mock targets to use new package locations
- [ ] 7.3 Verify all service tests pass after package refactoring
- [ ] 7.4 Verify all repository tests pass after package refactoring
- [ ] 7.5 Verify all controller tests pass after package refactoring
- [x] 7.1 Update test imports for new package structure
- [x] 7.2 Update mock targets to use new package locations
- [x] 7.3 Verify all service tests pass after package refactoring
- [x] 7.4 Verify all repository tests pass after package refactoring
- [x] 7.5 Verify all controller tests pass after package refactoring

## 8. Verification

- [ ] 8.1 Run `mvn clean compile` to verify package structure compiles
- [ ] 8.2 Run `mvn test` to verify all tests pass
- [ ] 8.3 Start application in local profile and verify startup
- [ ] 8.4 Test all API endpoints return correct `/api/v1` responses
- [ ] 8.5 Verify error responses are properly structured
- [ ] 8.6 Verify correlation IDs appear in responses and logs
- [x] 8.1 Run `mvn clean compile` to verify package structure compiles
- [x] 8.2 Run `mvn test` to verify all tests pass
- [x] 8.3 Start application in local profile and verify startup
- [x] 8.4 Test all API endpoints return correct `/api/v1` responses
- [x] 8.5 Verify error responses are properly structured
- [x] 8.6 Verify correlation IDs appear in responses and logs