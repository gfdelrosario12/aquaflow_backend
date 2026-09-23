## Purpose

Provides provider-neutral LoRaWAN network server integration for ChirpStack and external LNS providers, supporting webhook security, uplink normalization, and confirmed or unconfirmed downlink dispatch.

## ADDED Requirements

### Requirement: System SHALL provide provider-neutral LoRaWAN infrastructure interface
The system SHALL isolate LoRaWAN network server interactions behind `LoraNetworkServerClient` and `LoraWebhookHandler` interfaces, preventing provider-specific DTOs from leaking into the domain layer.

#### Scenario: Dispatch downlink via provider adapter
- **WHEN** domain service issues a `LoraDownlinkMessage` containing target DevEUI, FPort, base64 payload, and confirmation mode
- **THEN** system delegates dispatch to the active `LoraNetworkServerClient` adapter and returns a `LoraDownlinkResult`

#### Scenario: Handle network server integration errors gracefully
- **WHEN** LNS API returns error response or times out during downlink dispatch
- **THEN** system throws a structured `LoraIntegrationException` containing status code and error details without corrupting domain state

### Requirement: System SHALL validate webhook authentication headers
The system SHALL verify incoming network server webhook requests using configured secret tokens or signature headers before passing payloads to ingestion handlers.

#### Scenario: Reject unauthorized webhook request
- **WHEN** incoming webhook request lacks valid secret token header or fails signature verification
- **THEN** system rejects the webhook with HTTP 401 Unauthorized status

#### Scenario: Accept authenticated ChirpStack webhook
- **WHEN** ChirpStack sends an event webhook with valid authentication header
- **THEN** system accepts the request, normalizes the payload, and routes to telemetry ingestion

