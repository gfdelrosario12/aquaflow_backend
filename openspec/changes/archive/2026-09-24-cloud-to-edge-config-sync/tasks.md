## 1. Core Domain Entities, Enums, DTOs, and Repositories

- [ ] 1.1 Create `ConfigSyncStatus` enum (`PENDING`, `QUEUED`, `ACKNOWLEDGED`, `FAILED`, `TIMEOUT`)
- [ ] 1.2 Create `ConfigSyncTask` and `DownlinkQueueItem` JPA entities with correlation ID, versioning, and status tracking
- [ ] 1.3 Create DTOs: `ConfigSyncStatusResponse`, `ConfigSyncTriggerRequest`, `DownlinkQueueItemResponse`
- [ ] 1.4 Create `ConfigSyncTaskRepository` and `DownlinkQueueItemRepository`
- [x] 1.1 Create `ConfigSyncStatus` enum (`PENDING`, `QUEUED`, `ACKNOWLEDGED`, `FAILED`, `TIMEOUT`)
- [x] 1.2 Create `ConfigSyncTask` and `DownlinkQueueItem` JPA entities with correlation ID, versioning, and status tracking
- [x] 1.3 Create DTOs: `ConfigSyncStatusResponse`, `ConfigSyncTriggerRequest`, `DownlinkQueueItemResponse`
- [x] 1.4 Create `ConfigSyncTaskRepository` and `DownlinkQueueItemRepository`

## 2. Downlink Queue & Sync Service Layer

- [ ] 2.1 Implement `DownlinkQueueService` and `DownlinkQueueServiceImpl` for queueing downlinks, correlation ID generation, and payload serialization
- [ ] 2.2 Implement `EdgeNodeSyncService` and `EdgeNodeSyncServiceImpl` for orchestrating field-wide and node-level policy distribution
- [ ] 2.3 Implement correlation ID acknowledgement processing for incoming LoRaWAN uplink ACK frames
- [ ] 2.4 Implement scheduled retry and timeout handler for unacknowledged downlink tasks
- [ ] 2.5 Integrate `EdgeNodeSyncService` into `AwdConfigServiceImpl` to auto-trigger downlinks on policy update
- [x] 2.1 Implement `DownlinkQueueService` and `DownlinkQueueServiceImpl` for queueing downlinks, correlation ID generation, and payload serialization
- [x] 2.2 Implement `EdgeNodeSyncService` and `EdgeNodeSyncServiceImpl` for orchestrating field-wide and node-level policy distribution
- [x] 2.3 Implement correlation ID acknowledgement processing for incoming LoRaWAN uplink ACK frames
- [x] 2.4 Implement scheduled retry and timeout handler for unacknowledged downlink tasks
- [x] 2.5 Integrate `EdgeNodeSyncService` into `AwdConfigServiceImpl` to auto-trigger downlinks on policy update

## 3. REST Controller Endpoints

- [ ] 3.1 Implement `GET /api/v1/nodes/{nodeId}/config-sync/status` endpoint in `EdgeNodeSyncController`
- [ ] 3.2 Implement `POST /api/v1/nodes/{nodeId}/config-sync/trigger` endpoint in `EdgeNodeSyncController`
- [x] 3.1 Implement `GET /api/v1/nodes/{nodeId}/config-sync/status` endpoint in `EdgeNodeSyncController`
- [x] 3.2 Implement `POST /api/v1/nodes/{nodeId}/config-sync/trigger` endpoint in `EdgeNodeSyncController`

## 4. Verification and Integration Testing

- [ ] 4.1 Write unit tests for `DownlinkQueueServiceImpl` and `EdgeNodeSyncServiceImpl`
- [ ] 4.2 Write unit/WebMvc tests for `EdgeNodeSyncController`
- [ ] 4.3 Run `mvn clean compile` to verify clean compilation
- [ ] 4.4 Run `mvn test` to verify all unit and integration tests pass
- [x] 4.1 Write unit tests for `DownlinkQueueServiceImpl` and `EdgeNodeSyncServiceImpl`
- [x] 4.2 Write unit/WebMvc tests for `EdgeNodeSyncController`
- [x] 4.3 Run `mvn clean compile` to verify clean compilation
- [x] 4.4 Run `mvn test` to verify all unit and integration tests pass

