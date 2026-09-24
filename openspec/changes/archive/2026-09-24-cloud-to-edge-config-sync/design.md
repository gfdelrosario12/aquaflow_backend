## Context

See `proposal.md` for background motivation. Edge nodes execute autonomous AWD irrigation policies locally. The cloud manages `AutoIrrigationConfig` and transmission settings, which must be asynchronously synchronized to assigned edge nodes over LoRaWAN downlinks using `LoraNetworkServerClient` / `ChirpStackNetworkServerAdapter`.

## Goals / Non-Goals

**Goals:**
- Implement `EdgeNodeSyncService` for stateful sync orchestration and version control per node.
- Implement `DownlinkQueueService` for payload serialization, correlation ID generation, queue management, retry handling, and timeout processing.
- Persist sync status in `ConfigSyncState` and `DownlinkQueueItem` entities.
- Process uplink acknowledgements matching correlation IDs to transition sync state from `QUEUED` to `ACKNOWLEDGED`.
- Expose sync status REST API under `/api/v1/nodes/{nodeId}/config-sync/status` and manual trigger under `/api/v1/nodes/{nodeId}/config-sync/trigger`.
- Emit `CONFIG_SYNCED` and `NODE_STATUS_CHANGED` system events on state transitions.

**Non-Goals:**
- Direct synchronous execution confirmation on main REST HTTP request thread (LoRaWAN downlinks are fundamentally asynchronous).

## Decisions

### Decision 1: Dedicated Entities for Sync Task State and Downlink Queueing
- **Choice**: Create `ConfigSyncTask` (or `ConfigSyncState`) mapped to `config_sync_tasks` table and `DownlinkQueueItem` mapped to `downlink_queue_items`.
- **Rationale**: Keeps sync state decoupled from `EdgeNode` core domain entity while allowing detailed audit history of downlink correlation IDs, retry counts, payloads, and timestamps.

### Decision 2: Correlation ID-based Downlink Acknowledgement Tracking
- **Choice**: Embed unique UUID correlation IDs in binary/JSON downlink payloads and match incoming uplink ACK frames to set state to `ACKNOWLEDGED`.
- **Rationale**: Guarantees deterministic acknowledgement matching across asynchronous LoRaWAN uplink/downlink cycles.

### Decision 3: Retry & Timeout Processing via Scheduled Task
- **Choice**: Scheduled retry sweeper task in `DownlinkQueueServiceImpl` that checks items in `QUEUED` state past their timeout interval (e.g. 5 minutes). Increment retry count up to 3; if max retries exceeded, mark sync task as `FAILED` and emit an alert system event.
- **Rationale**: Prevents orphaned pending downlinks when nodes are unreachable or powered off.

## Risks / Trade-offs

- **[Risk] Unreachable edge node during configuration update** → **Mitigation**: Sync status transitions to `FAILED` after 3 retry timeouts; node safely continues executing its local cached valid policy; operator alerted via system event.
- **[Risk] High-frequency config updates flooding LoRaWAN downlink duty cycle** → **Mitigation**: `DownlinkQueueService` supersedes pending `QUEUED` tasks for the same node with the latest `configVersion`.

## Migration Plan

1. Create `ConfigSyncStatus` enum, `ConfigSyncTask` entity, `DownlinkQueueItem` entity, repositories, and DTOs.
2. Implement `DownlinkQueueService` and `EdgeNodeSyncService`.
3. Wire `AwdConfigServiceImpl` to trigger `EdgeNodeSyncService.syncConfigForField(fieldId)`.
4. Implement `EdgeNodeSyncController` mapped to `/api/v1/nodes/{nodeId}/config-sync`.
5. Implement unit and integration tests.

