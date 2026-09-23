## 1. Telemetry DTOs and Data Transfer Models

- [ ] 1.1 Create `SignalMetadataDto` and `DecodedTelemetryPayload` models in `com.aquaflow.backend.dto`
- [ ] 1.2 Create `TelemetryUplinkRequest`, `TelemetryUplinkResponse`, `TelemetryBatchUplinkRequest`, and `TelemetryBatchUplinkResponse` DTOs
- [ ] 1.3 Add validation constraints for timestamps, devEUI / node ID fields, and signal metadata
- [x] 1.1 Create `SignalMetadataDto` and `DecodedTelemetryPayload` models in `com.aquaflow.backend.dto`
- [x] 1.2 Create `TelemetryUplinkRequest`, `TelemetryUplinkResponse`, `TelemetryBatchUplinkRequest`, and `TelemetryBatchUplinkResponse` DTOs
- [x] 1.3 Add validation constraints for timestamps, devEUI / node ID fields, and signal metadata

## 2. Payload Decoder Abstraction and Implementations

- [ ] 2.1 Create `PayloadDecoder` interface and `PayloadDecoderRegistry` in `com.aquaflow.backend.domain`
- [ ] 2.2 Implement `ChirpStackPayloadDecoder` for ChirpStack webhook JSON parsing
- [ ] 2.3 Implement `TtnPayloadDecoder` for TTN uplink JSON parsing
- [ ] 2.4 Implement `JsonPayloadDecoder` for direct HTTP fallback payloads
- [x] 2.1 Create `PayloadDecoder` interface and `PayloadDecoderRegistry` in `com.aquaflow.backend.domain`
- [x] 2.2 Implement `ChirpStackPayloadDecoder` for ChirpStack webhook JSON parsing
- [x] 2.3 Implement `TtnPayloadDecoder` for TTN uplink JSON parsing
- [x] 2.4 Implement `JsonPayloadDecoder` for direct HTTP fallback payloads

## 3. Telemetry Ingestion Service & Idempotency Pipeline

- [ ] 3.1 Create `TelemetryIngestionService` interface and `TelemetryIngestionServiceImpl` in `com.aquaflow.backend.domain`
- [ ] 3.2 Implement DevEUI / nodeId identity resolution and `validateNodeActiveForOperations` operational invariant check
- [ ] 3.3 Implement LoRaWAN frame counter (`fCnt`) duplicate detection for idempotency
- [ ] 3.4 Implement timestamp drift validation and `EdgeNode` health/last-seen metrics update
- [x] 3.1 Create `TelemetryIngestionService` interface and `TelemetryIngestionServiceImpl` in `com.aquaflow.backend.domain`
- [x] 3.2 Implement DevEUI / nodeId identity resolution and `validateNodeActiveForOperations` operational invariant check
- [x] 3.3 Implement LoRaWAN frame counter (`fCnt`) duplicate detection for idempotency
- [x] 3.4 Implement timestamp drift validation and `EdgeNode` health/last-seen metrics update

## 4. REST API Controllers and Ingestion Endpoints

- [ ] 4.1 Create `TelemetryIngestionController` in `com.aquaflow.backend.api` mapped to `/api/v1/telemetry`
- [ ] 4.2 Implement `POST /api/v1/telemetry/uplink/{nodeId}` for individual node uplinks
- [ ] 4.3 Implement `POST /api/v1/telemetry/uplink` for generic/batch LNS webhook uplinks
- [x] 4.1 Create `TelemetryIngestionController` in `com.aquaflow.backend.api` mapped to `/api/v1/telemetry`
- [x] 4.2 Implement `POST /api/v1/telemetry/uplink/{nodeId}` for individual node uplinks
- [x] 4.3 Implement `POST /api/v1/telemetry/uplink` for generic/batch LNS webhook uplinks

## 5. Verification and Integration Testing

- [ ] 5.1 Write unit tests for `PayloadDecoder` implementations
- [ ] 5.2 Write unit tests for `TelemetryIngestionServiceImpl` covering frame counter deduplication, node health updates, and active guards
- [ ] 5.3 Write WebMvc/unit tests for `TelemetryIngestionController` endpoints
- [ ] 5.4 Run `mvn clean compile` and `mvn test` to verify zero errors across entire suite

- [x] 5.1 Write unit tests for `PayloadDecoder` implementations
- [x] 5.2 Write unit tests for `TelemetryIngestionServiceImpl` covering frame counter deduplication, node health updates, and active guards
- [x] 5.3 Write WebMvc/unit tests for `TelemetryIngestionController` endpoints
- [x] 5.4 Run `mvn clean compile` and `mvn test` to verify zero errors across entire suite
