## MODIFIED Requirements

### Requirement: System SHALL process normalized LoRaWAN uplinks via ChirpStack webhook controller
The system SHALL expose dedicated webhook endpoints for ChirpStack network server events (`POST /api/v1/thirdparty/chirpstack/webhook`) that validate authorization, normalize payloads before calling telemetry ingestion services, trigger edge node freshness updates, update zone water-level metrics, and stream updates to WebSocket clients.

#### Scenario: Receive ChirpStack uplink event
- **WHEN** ChirpStack posts a device uplink event webhook to `/api/v1/thirdparty/chirpstack/webhook` with event `up`
- **THEN** system validates authorization header, parses ChirpStack DTO, maps to provider-neutral `TelemetryUplinkRequest`, delegates to `TelemetryIngestionService`, updates node freshness, recalculates zone water level average, and publishes realtime WebSocket frames

#### Scenario: Handle invalid ChirpStack webhook signature or payload
- **WHEN** ChirpStack posts an unauthenticated or malformed webhook payload
- **THEN** system rejects request with HTTP 401/400 error and logs an audit security warning
