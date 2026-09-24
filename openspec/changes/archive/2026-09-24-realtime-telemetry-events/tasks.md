## 1. WebSocket Infrastructure and Security Configuration

- [x] 1.1 Enable Spring WebSocket (`WebSocketConfig`) registering `/api/v1/telemetry/stream` endpoint
- [x] 1.2 Implement `WebSocketSecurityInterceptor` to extract and validate JWT authentication tokens during WebSocket HTTP handshake

## 2. Event DTOs and Internal Publisher Architecture

- [x] 2.1 Define `SystemEventType` enum (`TELEMETRY_RECEIVED`, `NODE_STATUS_CHANGED`, `ZONE_TELEMETRY_UPDATED`, `IRRIGATION_DECISION_MADE`, `CONFIG_SYNCED`, `IRRIGATION_EXECUTION_UPDATED`, `ALARM_TRIGGERED`, `EMERGENCY_STOP_ACTIVATED`)
- [x] 2.2 Define standardized `SystemEvent` envelope model and specific payload DTOs
- [x] 2.3 Implement `SystemEventPublisher` interface and `SystemEventPublisherServiceImpl` wrapper around Spring `ApplicationEventPublisher`
- [x] 2.4 Implement `RealtimeEventPublisherService` and `@Async` `SystemEventListener` for non-blocking session broadcast

## 3. Ingestion Pipeline & Controller Integration

- [x] 3.1 Inject `SystemEventPublisher` into `TelemetryIngestionServiceImpl` to emit `TELEMETRY_RECEIVED` events asynchronously on ingestion
- [x] 3.2 Update `SecurityConfig` to permit `/api/v1/telemetry/stream` handshake endpoints with interceptor security

## 4. Verification and Integration Testing

- [x] 4.1 Write unit tests for `SystemEventPublisherServiceImpl` and `RealtimeEventPublisherServiceImpl`
- [x] 4.2 Write integration tests for `TelemetryStreamHandler` verifying WebSocket connection and event streaming
- [x] 4.3 Run `mvn clean compile` to verify clean compilation
- [x] 4.4 Run `mvn test` to verify all unit and integration tests pass


