## ADDED Requirements

### Requirement: System SHALL process normalized LoRaWAN uplinks via ChirpStack webhook controller
The system SHALL expose dedicated webhook endpoints for ChirpStack network server events (`POST /api/v1/thirdparty/chirpstack/webhook`) that validate authorization and normalize payloads before calling telemetry ingestion services.

#### Scenario: Receive ChirpStack uplink event
- **WHEN** ChirpStack posts a device uplink event webhook to `/api/v1/thirdparty/chirpstack/webhook` with event `up`
- **THEN** system validates authorization header, parses ChirpStack DTO, maps to provider-neutral `TelemetryUplinkRequest`, and delegates to `TelemetryIngestionService`

