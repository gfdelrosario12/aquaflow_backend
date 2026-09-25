# sensor-data-ingestion Specification

## Purpose
TBD - created by archiving change backend-architecture-baseline. Update Purpose after archive.
## Requirements
### Requirement: System SHALL ingest sensor data via MQTT
The system SHALL subscribe to MQTT topics for sensor readings and ingest incoming data from edge devices in real time.

#### Scenario: Receive sensor temperature reading
- **WHEN** edge device publishes temperature reading to MQTT topic
- **THEN** system receives, validates, and persists the sensor reading

### Requirement: System SHALL validate sensor readings
The system SHALL validate all incoming sensor data including sensor ID, timestamp, value range, and data type before persistence.

#### Scenario: Reject invalid sensor reading
- **WHEN** sensor reading contains out-of-range value or missing required fields
- **THEN** system rejects the reading and logs a validation error

### Requirement: System SHALL store sensor readings
The system SHALL persist validated sensor readings with high throughput to support time-series queries.

#### Scenario: Store batch of sensor readings
- **WHEN** multiple sensor readings are received within a short time window
- **THEN** system persists all readings in a batch operation

### Requirement: System SHALL alert on anomalous readings
The system SHALL detect sensor readings that exceed configured thresholds and generate alerts.

#### Scenario: High temperature alert
- **WHEN** temperature sensor reports value above critical threshold
- **THEN** system generates an alert notification for operations team

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

### Requirement: System SHALL process normalized LoRaWAN uplinks via ChirpStack webhook controller
The system SHALL expose dedicated webhook endpoints for ChirpStack network server events (`POST /api/v1/thirdparty/chirpstack/webhook`) that validate authorization, normalize payloads before calling telemetry ingestion services, trigger edge node freshness updates, update zone water-level metrics, and stream updates to WebSocket clients.

#### Scenario: Receive ChirpStack uplink event
- **WHEN** ChirpStack posts a device uplink event webhook to `/api/v1/thirdparty/chirpstack/webhook` with event `up`
- **THEN** system validates authorization header, parses ChirpStack DTO, maps to provider-neutral `TelemetryUplinkRequest`, delegates to `TelemetryIngestionService`, updates node freshness, recalculates zone water level average, and publishes realtime WebSocket frames

#### Scenario: Handle invalid ChirpStack webhook signature or payload
- **WHEN** ChirpStack posts an unauthenticated or malformed webhook payload
- **THEN** system rejects request with HTTP 401/400 error and logs an audit security warning

### Requirement: System SHALL emit asynchronous telemetry system events on ingestion
The system SHALL publish a `TELEMETRY_RECEIVED` system event asynchronously via internal application event publisher after successfully persisting incoming telemetry readings, ensuring real-time notification without delaying HTTP/webhook ingestion responses.

#### Scenario: Publish telemetry received event after successful persistence
- **WHEN** telemetry readings are successfully persisted for an edge node during uplink processing
- **THEN** system publishes an asynchronous `TELEMETRY_RECEIVED` system event containing node ID, zone ID, reading counts, and timestamp

