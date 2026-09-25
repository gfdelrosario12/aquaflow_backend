## MODIFIED Requirements

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

## ADDED Requirements

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
