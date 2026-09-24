## MODIFIED Requirements

### Requirement: System SHALL enforce role-based access control
API endpoints SHALL be protected by role-based authorization enforcing permissions across `ADMIN`, `OPERATOR`, `VIEWER`, and `EDGE_NODE` roles.

#### Scenario: Restricted admin access
- **WHEN** viewer-role user attempts to access admin endpoint
- **THEN** system rejects request with 403 Forbidden

#### Scenario: Operator permission for irrigation control
- **WHEN** an operator with `ROLE_OPERATOR` or `ROLE_ADMIN` submits a manual irrigation or emergency stop command
- **THEN** system authorizes and processes the request

#### Scenario: Reject unauthorized viewer modification
- **WHEN** a viewer with `ROLE_VIEWER` attempts to create or update fields, zones, or AWD threshold configurations
- **THEN** system rejects request with 403 Forbidden

## ADDED Requirements

### Requirement: System SHALL distinguish human operator and edge-node device authentication
The system SHALL evaluate human operator JWT identity tokens separately from edge-node device tokens or signed credentials.

#### Scenario: Edge node submits telemetry with device credentials
- **WHEN** an edge node posts telemetry using a valid device credential or node session token
- **THEN** system authenticates the request under `ROLE_EDGE_NODE` authority scope

#### Scenario: Reject human JWT token on node-only provisioning endpoint
- **WHEN** a request uses an invalid credential type or unauthenticated device token
- **THEN** system rejects the request with 401 Unauthorized

### Requirement: System SHALL authenticate and authorize LoRaWAN webhooks
The system SHALL verify incoming LoRaWAN webhook payloads against configured webhook secret headers (`X-Downlink-Secret` / `X-Webhook-Secret`) before accepting uplink events.

#### Scenario: Valid LoRaWAN webhook authentication
- **WHEN** ChirpStack or TTN sends a webhook payload with a matching secret header
- **THEN** system accepts and processes the uplink event

#### Scenario: Reject unauthenticated webhook
- **WHEN** a webhook request lacks a valid secret header
- **THEN** system rejects the request with 401 Unauthorized

### Requirement: System SHALL prevent sensitive credential disclosure in logs and API responses
The system SHALL mask commissioning tokens, LoRaWAN AppKeys, JWT signing secrets, and raw password strings so they are never written to application logs or returned in normal API JSON responses.

#### Scenario: Sanitize credentials in API response
- **WHEN** client queries edge node details or commissioning responses
- **THEN** system omits or masks raw secret keys and single-use tokens

### Requirement: System SHALL enforce security headers, CORS policies, and rate-limiting boundaries
The system SHALL include security headers (HSTS, CSP, X-Frame-Options, X-Content-Type-Options), restrict CORS origins, and apply rate-limiting boundaries on authentication endpoints.

#### Scenario: Return HTTP security headers
- **WHEN** client receives HTTP responses from backend API
- **THEN** response headers include `X-Content-Type-Options: nosniff`, `X-Frame-Options: DENY`, and strict CORS headers

