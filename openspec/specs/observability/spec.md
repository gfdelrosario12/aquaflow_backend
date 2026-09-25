# observability Specification

## Purpose
TBD - created by archiving change backend-architecture-baseline. Update Purpose after archive.
## Requirements
### Requirement: System SHALL expose health check endpoints
The system SHALL expose Spring Boot Actuator health endpoints at `/actuator/health`, `/actuator/health/liveness`, and `/actuator/health/readiness` reflecting database, Redis, and MQTT broker connectivity status.

#### Scenario: Application health check
- **WHEN** monitoring system queries `/actuator/health`
- **THEN** system returns status (UP/DOWN) for application and each dependency

#### Scenario: Kubernetes liveness probe query
- **WHEN** orchestrator queries `/actuator/health/liveness`
- **THEN** system returns HTTP 200 OK with status UP if application process and event loop are responsive

#### Scenario: Kubernetes readiness probe query
- **WHEN** orchestrator queries `/actuator/health/readiness`
- **THEN** system returns HTTP 200 OK with status UP only if database and broker connections are active

### Requirement: System SHALL expose Prometheus metrics
The system SHALL expose metrics at `/actuator/prometheus` for monitoring system performance and resource usage.

#### Scenario: Collect JVM metrics
- **WHEN** Prometheus scrapes `/actuator/prometheus`
- **THEN** system returns JVM memory, thread, GC, and HTTP request metrics

### Requirement: System SHALL serve OpenAPI documentation
The system SHALL generate and serve OpenAPI 3.0 documentation at `/api/v1/swagger-ui.html` or equivalent path.

#### Scenario: Access API documentation
- **WHEN** developer navigates to swagger UI URL
- **THEN** system renders interactive API documentation with all endpoints and schemas

### Requirement: System SHALL use structured logging
The system SHALL output logs in structured JSON format with timestamp, level, logger, message, and context fields.

#### Scenario: Structured log output
- **WHEN** application processes a request
- **THEN** log entry includes request path, method, response time, and correlation ID

### Requirement: System SHALL configure per-profile logging levels
The system SHALL set logging levels per profile (DEBUG for dev, WARN/ERROR for prod).

#### Scenario: Production logging
- **WHEN** application runs with `prod` profile
- **THEN** system logs at WARN or ERROR level only

### Requirement: System SHALL track request correlation IDs
The system SHALL generate and propagate unique correlation IDs across all log entries for a request.

#### Scenario: Correlation ID propagation
- **WHEN** request is processed
- **THEN** all log entries for that request share the same correlation ID

### Requirement: System SHALL provide structured error logs
Error log entries SHALL include correlation ID, HTTP status code, and request URI for debugging.

#### Scenario: Structured error log
- **WHEN** error occurs
- **THEN** log entry includes correlation ID, status code, and request URI

### Requirement: System SHALL expose REST endpoints for querying and exporting audit logs
The system SHALL expose REST endpoints under `/api/v1/audit/` to query irrigation audit logs (`GET /api/v1/audit/irrigation`), system audit logs (`GET /api/v1/audit/system`), and export audit records (`GET /api/v1/audit/export`).

#### Scenario: Query irrigation audit records
- **WHEN** client sends GET `/api/v1/audit/irrigation` with filter parameters
- **THEN** system returns a paginated list of irrigation audit records

#### Scenario: Query system audit records
- **WHEN** client sends GET `/api/v1/audit/system` with filter parameters
- **THEN** system returns a paginated list of system audit records

#### Scenario: Export audit records
- **WHEN** client sends GET `/api/v1/audit/export` with format parameter
- **THEN** system returns audit records formatted as CSV or JSON

### Requirement: System SHALL expose domain-specific Micrometer Prometheus metrics
The system SHALL register and record custom Micrometer metrics for telemetry ingestion throughput/latency, edge node online/offline counts, downlink command queue status/retries, active WebSocket subscriptions, and HikariCP database connection pool metrics.

#### Scenario: Prometheus metrics collection
- **WHEN** monitoring system scrapes `/actuator/prometheus`
- **THEN** output includes custom metrics for `aquaflow_telemetry_ingested_total`, `aquaflow_node_status_count`, `aquaflow_downlink_commands_total`, and `aquaflow_websocket_connections_active` alongside HikariCP pool metrics

### Requirement: System SHALL log request correlation IDs via MDC filter
The system SHALL extract or generate `X-Correlation-ID` headers for incoming HTTP requests and populate Mapped Diagnostic Context (MDC) so that all structured JSON logs produced during request handling include correlation IDs.

#### Scenario: Request log correlation ID inclusion
- **WHEN** HTTP request is received with `X-Correlation-ID: 12345`
- **THEN** all JSON log statements emitted during request processing contain `"correlationId": "12345"`

