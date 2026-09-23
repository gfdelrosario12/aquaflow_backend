## ADDED Requirements

### Requirement: System SHALL manage EdgeNode lifecycle transitions
The system SHALL maintain formal lifecycle states for `EdgeNode` entities (`UNREGISTERED`, `PROVISIONED`, `COMMISSIONED`, `ACTIVE`, `DECOMMISSIONED`, `REPLACED`) and enforce valid state transition workflows (`commission`, `decommission`, `replace`).

#### Scenario: Commission edge node
- **WHEN** client submits POST `/api/v1/nodes/{id}/commission` with valid commissioning parameters
- **THEN** system transitions node lifecycle state from `PROVISIONED` to `COMMISSIONED`, generates a single-use commissioning credential token, and returns the response without exposing raw cryptographic secrets

#### Scenario: Decommission edge node
- **WHEN** client submits POST `/api/v1/nodes/{id}/decommission`
- **THEN** system transitions node state to `DECOMMISSIONED`, unassigns active monitoring points, and invalidates active edge session tokens

#### Scenario: Replace faulty edge node
- **WHEN** client submits POST `/api/v1/nodes/{id}/replace` with replacement node ID
- **THEN** system transfers monitoring point assignments and configuration from faulty node to replacement node, marks original node as `REPLACED`, and commissions replacement node

### Requirement: System SHALL configure transmission parameters
The system SHALL support updating transmission frequency interval (seconds), transmit power level (dBm), and duty cycle percentage on `EdgeNode` entities.

#### Scenario: Update transmission configuration
- **WHEN** client submits PUT `/api/v1/nodes/{id}/transmission` with valid transmission parameters
- **THEN** system updates the node's transmission properties and flags configuration for edge node sync

### Requirement: System SHALL expose node health and telemetry queries
The system SHALL provide endpoints for querying node health status, latest telemetry readings, and paginated historical telemetry.

#### Scenario: Query node health metrics
- **WHEN** client requests GET `/api/v1/nodes/{id}/health`
- **THEN** system returns battery level, solar voltage, signal strength (dBm), and last heartbeat timestamp for the node

#### Scenario: Query node latest and historical telemetry
- **WHEN** client requests GET `/api/v1/nodes/{id}/telemetry/latest` or GET `/api/v1/nodes/{id}/telemetry/history`
- **THEN** system returns latest or paginated historical telemetry readings recorded by the node's assigned monitoring points

