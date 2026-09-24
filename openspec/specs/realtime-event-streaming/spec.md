# realtime-event-streaming Specification

## Purpose

Provides real-time event streaming over WebSocket connections at `/api/v1/telemetry/stream` for AquaFlow clients, emitting normalized events for telemetry, node status changes, zone aggregates, irrigation decisions, config sync, execution updates, alarms, and emergency stops.

## Requirements

### Requirement: System SHALL expose real-time WebSocket event stream endpoint
The system SHALL provide a WebSocket endpoint at `/api/v1/telemetry/stream` allowing authenticated clients to receive normalized system events in real time.

#### Scenario: Authenticated client connects to stream
- **WHEN** client initiates a WebSocket handshake at `/api/v1/telemetry/stream` with valid authorization bearer token
- **THEN** system accepts the WebSocket connection, registers the session, and begins streaming authorized system events

#### Scenario: Reject unauthenticated WebSocket handshake
- **WHEN** client attempts WebSocket connection at `/api/v1/telemetry/stream` without a valid token or credentials
- **THEN** system rejects the connection handshake with HTTP 401 Unauthorized status

### Requirement: System SHALL publish standardized system event types and payloads
The system SHALL emit events with standardized envelope fields (`eventId`, `eventType`, `timestamp`, `aggregateId`, `payload`) covering `TELEMETRY_RECEIVED`, `NODE_STATUS_CHANGED`, `ZONE_TELEMETRY_UPDATED`, `IRRIGATION_DECISION_MADE`, `CONFIG_SYNCED`, `IRRIGATION_EXECUTION_UPDATED`, `ALARM_TRIGGERED`, and `EMERGENCY_STOP_ACTIVATED`.

#### Scenario: Broadcast telemetry received event to connected clients
- **WHEN** telemetry is successfully ingested for an edge node
- **THEN** system constructs a normalized `TELEMETRY_RECEIVED` system event and streams it to authorized WebSocket clients

#### Scenario: Broadcast emergency stop event to connected clients
- **WHEN** an emergency stop is activated for an irrigation zone or node
- **THEN** system constructs an `EMERGENCY_STOP_ACTIVATED` event with high priority and immediately broadcasts it to all connected clients

### Requirement: System SHALL isolate real-time event broadcasting from ingestion persistence transactions
The system SHALL deliver events to WebSocket clients asynchronously such that slow client connections, dropped WebSocket sockets, or event rendering errors NEVER block or cause database rollback of core telemetry ingestion or node state persistence.

#### Scenario: Telemetry ingestion succeeds when WebSocket client disconnects abruptly
- **WHEN** a telemetry uplink is received while a WebSocket client connection drops or fails
- **THEN** system completes telemetry database persistence cleanly and logs a warning for the dropped WebSocket socket without failing the HTTP/webhook uplink response

