## Context

See `proposal.md` for background motivation. AquaFlow requires real-time streaming of telemetry, status changes, irrigation decisions, alarms, and emergency stops over WebSocket without compromising database transaction integrity.

## Goals / Non-Goals

**Goals:**
- Implement WebSocket endpoint at `/api/v1/telemetry/stream` with JWT handshake authentication.
- Build an internal asynchronous event publisher (`SystemEventPublisher`) using Spring `ApplicationEventPublisher` and `@Async`.
- Standardize event types (`SystemEventType`) and payload structure (`SystemEvent`).
- Ensure complete failure isolation so WebSocket delivery failures never fail telemetry ingestion transactions.
- Filter events based on field authorization permissions associated with connected client sessions.

**Non-Goals:**
- External message broker cluster setup (Kafka/RabbitMQ) for multi-node backend scale-out (local Spring event bus & WebSocket session registry for single instance / scale unit).

## Decisions

### Decision 1: Spring Native WebSocket Handler (`WebSocketHandler`)
- **Choice**: Implement native Spring `TextWebSocketHandler` (`TelemetryStreamHandler`) registered under `/api/v1/telemetry/stream`.
- **Rationale**: Provides fine-grained control over connection lifecycle, handshake interceptors, and low-overhead JSON frame broadcasting without heavy STOMP protocol overhead.

### Decision 2: Spring ApplicationEventPublisher with `@Async`
- **Choice**: `SystemEventPublisherServiceImpl` publishes `SystemEvent` domain events via Spring's `ApplicationEventPublisher`. `SystemEventListener` listens using `@Async`.
- **Rationale**: Decouples the ingestion pipeline from real-time delivery. Database transactions complete cleanly before async event dispatch.

### Decision 3: Standardized Event Envelope Schema
- **Choice**: All real-time messages share a unified JSON structure:
  ```json
  {
    "eventId": "uuid-v4",
    "eventType": "TELEMETRY_RECEIVED",
    "timestamp": "2026-09-24T08:00:00Z",
    "aggregateId": "NODE-001",
    "fieldId": 1,
    "payload": { ... }
  }
  ```

### Decision 4: Safe Non-Blocking WebSocket Session Management
- **Choice**: `RealtimeEventPublisherServiceImpl` maintains a `ConcurrentHashMap` of active sessions. Session broadcasts iterate safely with individual try-catch blocks, automatically removing stale or broken sessions upon socket errors.

## Risks / Trade-offs

- **[Risk] High memory usage with many connected WebSocket clients** → **Mitigation**: Enforce session limits and connection heartbeat timeouts.
- **[Risk] Unauthenticated WebSocket connection attempts** → **Mitigation**: Implement `WebSocketSecurityInterceptor` checking JWT token in query parameters (`?token=...`) during HTTP handshake.

## Migration Plan

1. Configure WebSocket infrastructure (`WebSocketConfig`, `WebSocketSecurityInterceptor`).
2. Create `SystemEventType`, `SystemEvent`, and publisher SPI interfaces.
3. Implement `TelemetryStreamHandler` and `RealtimeEventPublisherService`.
4. Inject `SystemEventPublisher` into `TelemetryIngestionServiceImpl` and node status services.
5. Run unit and integration tests.

