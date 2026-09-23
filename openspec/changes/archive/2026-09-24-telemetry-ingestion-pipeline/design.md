## Context

The AquaFlow system requires a dedicated telemetry ingestion pipeline that ingests sensor data from LoRaWAN network servers (such as ChirpStack or TTN) as well as direct HTTP uplinks. While `EdgeNode`, `MonitoringPoint`, and `TelemetryReading` entities exist, the ingestion flow must be transport-decoupled, idempotent against duplicate LoRaWAN frame counters, and capable of updating `EdgeNode` health metrics.

See `proposal.md` for background motivation.

## Goals / Non-Goals

**Goals:**
- Implement transport-decoupled payload decoding using a strategy pattern (`PayloadDecoder` registry).
- Support single (`POST /api/v1/telemetry/uplink/{nodeId}`) and batch/generic (`POST /api/v1/telemetry/uplink`) HTTP endpoints.
- Perform frame counter duplicate detection (`fCnt`) per `EdgeNode` to ensure idempotency.
- Validate timestamp drift relative to server clock.
- Automatically update `EdgeNode` last heartbeat, signal metrics (RSSI/SNR), battery level, and solar voltage.
- Reject telemetry from inactive, decommissioned, or unknown edge nodes using `EdgeNodeRegistryService.validateNodeActiveForOperations`.

**Non-Goals:**
- Low-level binary gateway packet decoding (e.g. UDP Semtech forwarder protocol).
- Cryptographic decryption of LoRaWAN AppSKey/NwkSKey payloads (performed by external LNS).

## Decisions

### Decision 1: Transport Decoupling via PayloadDecoder Strategy Pattern
- **Rationale**: Webhook payloads from ChirpStack, The Things Network (TTN), and custom HTTP edge devices differ in JSON layout. By defining a `PayloadDecoder` interface and `PayloadDecoderRegistry`, transport/webhook handling is isolated from core domain telemetry ingestion.
- **Alternatives Considered**: Monolithic controller with conditional `if-else` parsing for each LNS vendor (rejected due to tight coupling and poor extensibility).

### Decision 2: Idempotent Ingestion via Frame Counter Tracking
- **Rationale**: LoRaWAN network servers retransmit uplinks on acknowledgment failures. The pipeline tracks the highest or recent `fCnt` per node ID to discard duplicate frames without creating redundant `TelemetryReading` database rows.
- **Alternatives Considered**: Storing duplicate readings with unique UUID generation (rejected due to database bloat and corrupted analytics).

### Decision 3: Atomic Ingestion & Node Health Update
- **Rationale**: Each uplink transaction persists `TelemetryReading` entities and updates `EdgeNode` `healthMetrics` and `lastHeartbeat` atomically in a single `@Transactional` service invocation.
- **Alternatives Considered**: Asynchronous event publishing for health updates (rejected for simplicity and consistent state in initial baseline).

## Risks / Trade-offs

- **Risk**: Clock skew between edge nodeRTC and cloud server.
  - **Mitigation**: Timestamp validation flags readings with timestamps > 24 hours in the past or > 5 minutes in the future, substituting server arrival time if invalid.
- **Risk**: Malformed base64/hex payload from unconfigured device profile.
  - **Mitigation**: Exception catching within decoders returning structured error response in `TelemetryUplinkResponse` without crashing ingestion pipeline.

## Migration Plan

1. Implement telemetry request and response DTOs (`TelemetryUplinkRequest`, `TelemetryUplinkResponse`, `TelemetryBatchUplinkRequest`, `TelemetryBatchUplinkResponse`, `SignalMetadataDto`).
2. Create `PayloadDecoder` interface, `ChirpStackPayloadDecoder`, `TtnPayloadDecoder`, and `JsonPayloadDecoder`.
3. Implement `TelemetryIngestionService` and `TelemetryIngestionServiceImpl`.
4. Create `TelemetryIngestionController` exposing `/api/v1/telemetry/uplink` and `/api/v1/telemetry/uplink/{nodeId}`.
5. Write unit tests for decoders, service lifecycle guards, and controller endpoints.
6. Verify build and test suite execution with `mvn clean test`.

