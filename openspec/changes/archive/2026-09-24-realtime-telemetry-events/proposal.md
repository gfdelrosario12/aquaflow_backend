## Why

AquaFlow monitoring dashboards, operational control views, and mobile clients require instant real-time visibility into telemetry readings, node health state changes, zone aggregates, irrigation execution progress, and critical alerts/emergency stops. Currently, clients rely on REST polling. Implementing a dedicated WebSocket endpoint (`/api/v1/telemetry/stream`) driven by an asynchronous internal event publisher provides low-latency event delivery without introducing tight coupling or risking database transaction rollbacks during stream disconnections.

## What Changes

- **WebSocket Stream Endpoint (`/api/v1/telemetry/stream`)**: Establish a WebSocket handler for real-time bidirectional/unidirectional telemetry and system event streaming.
- **Asynchronous Internal Event Bus (`SystemEventPublisher`)**: Create Spring `ApplicationEventPublisher` wrappers to publish typed system events asynchronously (`@Async` event listeners) so real-time delivery failures never cause ingestion database transactions to fail.
- **Stable Event Schemas & Types**: Standardize event payload DTOs for `TELEMETRY_RECEIVED`, `NODE_STATUS_CHANGED`, `ZONE_TELEMETRY_UPDATED`, `IRRIGATION_DECISION_MADE`, `CONFIG_SYNCED`, `IRRIGATION_EXECUTION_UPDATED`, `ALARM_TRIGGERED`, and `EMERGENCY_STOP_ACTIVATED`.
- **Client Authorization & Filtering**: Authenticate WebSocket handshake connections (via JWT token) and restrict event broadcasting so clients only receive events for authorized fields or tenant resources.

## Capabilities

### New Capabilities
- `realtime-event-streaming`: Real-time WebSocket event streaming at `/api/v1/telemetry/stream` covering telemetry, node status, zone updates, decisions, execution, alarms, and emergency stops.

### Modified Capabilities
- `sensor-data-ingestion`: Emits asynchronous `TELEMETRY_RECEIVED` system events upon successful telemetry ingestion without delaying ingestion responses.

## Impact

- **Configuration**: `WebSocketConfig` registering `/api/v1/telemetry/stream`.
- **Infrastructure**: `WebSocketSecurityInterceptor` for JWT token authentication during handshake.
- **Events & DTOs**: `SystemEvent`, `SystemEventType`, `TelemetryReceivedEvent`, `NodeStatusChangedEvent`, `ZoneTelemetryUpdatedEvent`, `IrrigationExecutionEvent`, `AlarmEvent`, `EmergencyStopEvent`.
- **Services**: `RealtimeEventPublisher`, `SystemEventPublisherServiceImpl`, `TelemetryStreamHandler`.

