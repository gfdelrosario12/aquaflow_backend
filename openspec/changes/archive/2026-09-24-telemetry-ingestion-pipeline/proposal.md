## Why

AquaFlow requires a robust telemetry ingestion pipeline to process real-time sensor data uploaded via LoRaWAN network servers (such as ChirpStack or The Things Network) as well as direct HTTP fallback uplinks from edge nodes. A flexible, transport-decoupled pipeline ensures frame counter duplicate detection, timestamp validation, payload decoding, device identity resolution, signal metadata tracking, and EdgeNode status updates without coupling domain logic to specific network server webhooks.

## What Changes

- **Telemetry Ingestion Pipeline Service & Transport Decoupling**: Implement `TelemetryIngestionService` and transport-decoupled payload decoders (`PayloadDecoderService`, `LoRaWANPayloadDecoder`, `JsonPayloadDecoder`) separating ChirpStack/TTN webhook mapping from core domain telemetry ingestion.
- **Node Identity & Security Resolution**: Resolve edge nodes by communication identity (MAC address, serial number, DevEUI) or `nodeId`, enforcing node active state validation (`validateNodeActiveForOperations`) to reject unauthorized or decommissioned devices.
- **LoRaWAN Frame Counter & Duplicate Detection**: Implement frame counter cache / repository tracking to achieve idempotent uplink processing and discard duplicate LoRaWAN frames.
- **Metadata & Last-Seen Updates**: Automatically update `EdgeNode` last heartbeat, signal strength (RSSI/SNR), battery level, and solar voltage upon valid telemetry ingestion.
- **Batch & Individual Uplink REST APIs**:
  - `POST /api/v1/telemetry/uplink`: Ingest batch or generic LoRaWAN / HTTP telemetry payload.
  - `POST /api/v1/telemetry/uplink/{nodeId}`: Ingest telemetry payload targeting a specific node.

## Capabilities

### New Capabilities
<!-- None -->

### Modified Capabilities
- `sensor-data-ingestion`: Add LoRaWAN and HTTP telemetry ingestion pipeline endpoints, payload decoding abstraction, frame counter duplicate detection, timestamp validation, batch/individual uplinks, and structured ingestion response.
- `device-management`: Automatically update `EdgeNode` communication metrics, battery level, solar voltage, signal strength, and last heartbeat timestamp during telemetry processing.

## Impact

- **New REST Controllers & Endpoints**: `com.aquaflow.backend.api.TelemetryIngestionController` handling `POST /api/v1/telemetry/uplink` and `POST /api/v1/telemetry/uplink/{nodeId}`.
- **Domain & Service Layer**: `com.aquaflow.backend.domain.TelemetryIngestionService`, `TelemetryIngestionServiceImpl`, `PayloadDecoderRegistry`, `PayloadDecoder`.
- **DTOs**: `TelemetryUplinkRequest`, `TelemetryUplinkResponse`, `TelemetryBatchUplinkRequest`, `TelemetryBatchUplinkResponse`, `SignalMetadataDto`, `DecodedTelemetryPayload`.

