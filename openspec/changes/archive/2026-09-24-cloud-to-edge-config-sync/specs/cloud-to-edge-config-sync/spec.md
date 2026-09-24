## Purpose

Provides cloud-to-edge configuration synchronization using LoRaWAN downlink queueing, asynchronous transmission, correlation ID tracking, delivery retry policies, timeout management, acknowledgement processing, and sync status REST APIs.

## ADDED Requirements

### Requirement: System SHALL queue configuration downlinks for asynchronous edge node delivery
The system SHALL support queueing validated `AutoIrrigationConfig` and transmission settings into a LoRaWAN downlink queue for asynchronous transmission to target edge nodes.

#### Scenario: Queue configuration update for edge node
- **WHEN** field configuration is updated or manually synced for an edge node
- **THEN** system serializes the policy configuration, generates a unique correlation ID, queues a LoRaWAN downlink message, and sets sync status to `QUEUED`

### Requirement: System SHALL track downlink delivery status and process edge acknowledgements
The system SHALL track configuration sync states (`PENDING`, `QUEUED`, `ACKNOWLEDGED`, `FAILED`, `TIMEOUT`) and transition to `ACKNOWLEDGED` upon receiving a valid downlink ACK from the target edge node.

#### Scenario: Process successful configuration acknowledgement
- **WHEN** edge node sends an uplink containing a matching correlation ID acknowledgement
- **THEN** system updates the sync task status to `ACKNOWLEDGED`, updates `lastSyncedAt` timestamp, and marks configuration version as synchronized

#### Scenario: Handle delivery retry and timeout failure
- **WHEN** downlink transmission fails or exceeds timeout without receiving an ACK after max retry limit (3 retries)
- **THEN** system transitions sync status to `FAILED`, logs delivery failure details, and emits a `NODE_STATUS_CHANGED` alarm event

### Requirement: Edge nodes SHALL retain last known valid configuration during cloud disconnection
The system SHALL enforce edge autonomy by requiring edge nodes to retain and continue operating under their last verified valid configuration when cloud downlinks fail or connection is lost.

#### Scenario: Edge node operates during cloud disconnection
- **WHEN** cloud connection or downlink transmission is interrupted
- **THEN** cloud status reflects `FAILED` or `TIMEOUT` sync state while edge node continues executing its local cached configuration policy

### Requirement: System SHALL expose configuration sync state via REST API
The system SHALL provide REST endpoints for querying sync status and triggering manual sync retries for an edge node.

#### Scenario: Query configuration sync status for edge node
- **WHEN** client requests `GET /api/v1/nodes/{nodeId}/config-sync/status`
- **THEN** system returns current sync state, config version, last attempt timestamp, correlation ID, and delivery history

#### Scenario: Manually trigger configuration sync for edge node
- **WHEN** client submits `POST /api/v1/nodes/{nodeId}/config-sync/trigger`
- **THEN** system enqueues a new downlink sync task and returns the initial `QUEUED` sync task details

