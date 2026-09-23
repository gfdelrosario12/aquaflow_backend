## Why

Autonomous field hardware requires a dedicated Edge Node Registry to manage device onboarding, lifecycle states (e.g. UNREGISTERED, PROVISIONED, COMMISSIONED, ACTIVE, DECOMMISSIONED, REPLACED), hardware identity, transmission parameters, and health monitoring. A robust registry prevents unauthorized edge devices from injecting telemetry or executing cloud synchronization while safeguarding sensitive LoRaWAN keys and provisioning tokens.

## What Changes

- **Edge Node Lifecycle Domain Service & Business Invariants**: Implement `EdgeNodeRegistryService` to manage node lifecycle transitions (`commission`, `decommission`, `replace`), transmission parameter configurations, and health monitoring.
- **Secure Commissioning & Credential Security**: Generate secure single-use commissioning tokens for field provisioning without exposing LoRaWAN AppKeys or raw cryptographic secrets in standard API DTO responses.
- **REST API Endpoints for Node Operations**:
  - `POST /api/v1/nodes`: Register new hardware edge node
  - `GET /api/v1/nodes`: List/search edge nodes with lifecycle and health filters
  - `GET /api/v1/nodes/{id}`: Retrieve detailed edge node metadata
  - `PUT /api/v1/nodes/{id}`: Update node identity and configuration parameters
  - `POST /api/v1/nodes/{id}/commission`: Commission edge node and issue secure credentials
  - `POST /api/v1/nodes/{id}/decommission`: Decommission node and invalidate active sessions
  - `POST /api/v1/nodes/{id}/replace`: Reassign monitoring points and configuration from a faulty node to a replacement node
  - `PUT /api/v1/nodes/{id}/transmission`: Update transmission interval, power level, and duty cycle parameters
  - `GET /api/v1/nodes/{id}/health`: Retrieve battery level, solar voltage, RSSI/SNR, and heartbeat health metrics
  - `GET /api/v1/nodes/{id}/telemetry/latest`: Fetch latest telemetry readings for node's monitoring points
  - `GET /api/v1/nodes/{id}/telemetry/history`: Fetch paginated historical telemetry for node's monitoring points

## Capabilities

### New Capabilities
<!-- None -->

### Modified Capabilities
- `device-management`: Added edge node lifecycle state transitions, secure commissioning token generation, transmission configuration, node replacement workflow, and health/telemetry endpoints.
- `edge-irrigation-core`: Added lifecycle invariants prohibiting un-commissioned or decommissioned nodes from ingesting telemetry or executing autonomous irrigation synchronization.

## Impact

- **New & Enhanced REST APIs**:
  - `POST /api/v1/nodes`, `GET /api/v1/nodes`, `GET /api/v1/nodes/{id}`, `PUT /api/v1/nodes/{id}`
  - `POST /api/v1/nodes/{id}/commission`, `POST /api/v1/nodes/{id}/decommission`, `POST /api/v1/nodes/{id}/replace`
  - `PUT /api/v1/nodes/{id}/transmission`
  - `GET /api/v1/nodes/{id}/health`, `GET /api/v1/nodes/{id}/telemetry/latest`, `GET /api/v1/nodes/{id}/telemetry/history`
- **Domain Services & DTOs**:
  - `com.aquaflow.backend.domain.EdgeNodeRegistryService`, `EdgeNodeRegistryServiceImpl`
  - `com.aquaflow.backend.api.EdgeNodeRegistryController`
  - DTOs: `RegisterNodeRequest`, `CommissionNodeRequest`, `CommissionNodeResponse`, `ReplaceNodeRequest`, `UpdateTransmissionRequest`

