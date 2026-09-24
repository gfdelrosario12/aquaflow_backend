## Why

AquaFlow cloud policy updates (`AutoIrrigationConfig` and transmission settings) must be reliably distributed to edge nodes deployed across remote agricultural fields over bandwidth-constrained LoRaWAN links. Because LoRaWAN downlinks are asynchronous, class-A/Class-C duty-cycle constrained, and prone to temporary connectivity losses, the cloud cannot assume a configuration policy has been applied merely because a message was queued. 

To guarantee eventual consistency without risking unsafe edge execution, AquaFlow requires an asynchronous configuration synchronization pipeline (`EdgeNodeSyncService` and `DownlinkQueueService`) that queues downlinks, tracks correlation IDs, handles retries and timeouts, monitors pending/acknowledged/failed delivery states, and exposes synchronization state to API consumers while allowing edge nodes to safely retain their last known valid policy during cloud disconnection.

## What Changes

- Create `EdgeNodeSyncService` to orchestrate policy distribution, version matching, and status management per edge node.
- Create `DownlinkQueueService` to encapsulate LoRaWAN downlink payload formatting, queueing, correlation ID management, and retransmission policies.
- Track synchronization states: `PENDING`, `QUEUED`, `ACKNOWLEDGED`, `FAILED`, and `TIMEOUT`.
- Manage configuration versioning (`configVersion`), correlation IDs, and delivery retry limits (e.g., max 3 retries before marking as `FAILED`).
- Process LoRaWAN downlink acknowledgements (`downlink_ack`) from edge nodes to update sync state to `ACKNOWLEDGED`.
- Handle edge node fallback guarantees ensuring nodes retain and execute their last known valid configuration when disconnected from cloud.
- Expose REST API endpoints to retrieve edge node configuration sync status (`GET /api/v1/nodes/{nodeId}/config-sync/status`) and manually trigger/retry sync (`POST /api/v1/nodes/{nodeId}/config-sync/trigger`).
- Publish `CONFIG_SYNCED` and `NODE_STATUS_CHANGED` system events on sync status transitions.

## Capabilities

### New Capabilities

- `cloud-to-edge-config-sync`: Distributes cloud policy and transmission configurations to edge nodes via LoRaWAN downlink queueing, retry mechanisms, acknowledgement tracking, and sync state exposure.

### Modified Capabilities

- `awd-configuration-management`: Connects policy updates (`AutoIrrigationConfig`) to asynchronous edge node synchronization triggers.

## Impact

- **Domain & Infrastructure**: New domain services (`EdgeNodeSyncService`, `DownlinkQueueService`) and persistence entities (`ConfigSyncTask`, `DownlinkQueueItem`).
- **LoRaWAN Infrastructure**: Integrates with existing `LoraNetworkServerClient` / `ChirpStackNetworkServerAdapter` for queueing downlinks and handling uplink ACKs.
- **REST APIs**: Exposes sync status and manual sync trigger endpoints on `/api/v1/nodes/{nodeId}/config-sync`.
- **Event System**: Emits system events on sync transitions.

