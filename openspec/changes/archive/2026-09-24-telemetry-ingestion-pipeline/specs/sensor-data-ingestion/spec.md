## ADDED Requirements

### Requirement: System SHALL ingest telemetry via LoRaWAN and HTTP uplink endpoints
The system SHALL provide HTTP REST endpoints `POST /api/v1/telemetry/uplink` and `POST /api/v1/telemetry/uplink/{nodeId}` supporting single and batch telemetry uplinks from LoRaWAN network servers or direct HTTP edge devices.

#### Scenario: Ingest valid individual telemetry uplink
- **WHEN** client submits a valid uplink payload to `POST /api/v1/telemetry/uplink/{nodeId}` for an active node
- **THEN** system decodes sensor measurements, persists `TelemetryReading` records, updates node status, and returns HTTP 200 with ingestion results

#### Scenario: Ingest batch telemetry uplinks
- **WHEN** client submits a batch uplink request to `POST /api/v1/telemetry/uplink` containing multiple node uplinks
- **THEN** system processes each uplink, persists valid telemetry readings, updates corresponding nodes, and returns batch ingestion summary

#### Scenario: Reject telemetry from unregistered or decommissioned node
- **WHEN** uplink request references a node ID or DevEUI that is unregistered, decommissioned, or replaced
- **THEN** system rejects ingestion for that node with error code `NODE_NOT_ACTIVE` and returns unsuccessful status detail

### Requirement: System SHALL decode payload and validate LoRaWAN frame counter idempotency
The system SHALL isolate network server webhook format handling, decode binary/JSON payloads using a payload decoding service, and deduplicate requests based on LoRaWAN frame counters (`fCnt`).

#### Scenario: Decode binary LoRaWAN payload using codec
- **WHEN** a raw base64 or hex encoded payload is received with FPort and device profile metadata
- **THEN** payload decoder transforms binary bytes into structured telemetry measurements (soil moisture, temperature, battery, solar voltage)

#### Scenario: Ignore duplicate LoRaWAN frame counter
- **WHEN** an uplink is received with a frame counter (`fCnt`) matching a previously processed frame for the same node
- **THEN** system marks the uplink as duplicate, skips creating duplicate `TelemetryReading` entries, and returns an idempotent success response

### Requirement: System SHALL record signal metadata and timestamp validation
The system SHALL validate uplink timestamps against acceptable drift thresholds and store signal metadata (RSSI, SNR, gateway ID, frequency) associated with telemetry ingestion events.

#### Scenario: Validate timestamp within acceptable window
- **WHEN** telemetry payload timestamp is within acceptable operational tolerance relative to server clock
- **THEN** system accepts the reading and records the edge timestamp

#### Scenario: Capture radio signal metadata
- **WHEN** LoRaWAN uplink contains gateway reception metadata including RSSI and SNR
- **THEN** system extracts signal metrics and updates `EdgeNode` health metrics

