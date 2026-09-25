# autonomous-edge-system-integration Specification

## Purpose
Defines full end-to-end integration contracts across ESP32 LoRaWAN ingestion, edge node health monitoring, zone aggregation, realtime WebSocket broadcasts, edge decision audit reporting, cloud-to-edge configuration synchronization, and irrigation command state machine execution.
## Requirements
### Requirement: System SHALL execute end-to-end happy-path integration flow from ESP32 telemetry to WebSocket stream and zone metrics
The system SHALL ingest LoRaWAN uplink telemetry payloads from ChirpStack, parse water level and battery level, update edge node status and freshness timestamp, compute aggregated zone water metrics, persist records, and broadcast live updates over WebSocket STOMP topics.

#### Scenario: Successful telemetry ingestion and zone broadcast
- **WHEN** ChirpStack posts a valid LoRaWAN telemetry uplink payload for a registered node
- **THEN** system decodes payload, updates node last-seen timestamp, stores telemetry record, updates zone average water level, and publishes WebSocket frames to `/topic/telemetry/{zoneId}` and `/topic/nodes/{nodeId}/status`

### Requirement: System SHALL enforce zero cloud autonomous decision-making
The system SHALL accept and audit edge-initiated decision reports but MUST NOT independently generate autonomous valve open/close commands from cloud background jobs or analytics.

#### Scenario: Edge decision report processing
- **WHEN** edge node executes an autonomous irrigation decision and transmits an edge decision frame
- **THEN** system logs the decision record in `irrigation_decision` and `irrigation_audit_log` tables, broadcasts `/topic/decisions`, and dispatches no cloud-driven autonomous commands

### Requirement: System SHALL execute cloud-to-edge AWD configuration sync with versioning and downlink delivery
The system SHALL increment configuration version upon user AWD threshold updates, create pending config sync tasks, format ChirpStack downlink payloads, dispatch downlinks, and mark task completed upon edge ACK frame receipt.

#### Scenario: AWD configuration update and downlink ACK
- **WHEN** user updates AWD parameters for a zone via REST API
- **THEN** system increments config version, enqueues downlink task, dispatches ChirpStack downlink, and marks config sync task completed when edge ACK frame is received

### Requirement: System SHALL prioritize emergency stop commands over pending queued commands
The system SHALL immediately set emergency stop active status for target field/zone/node, reject new manual irrigation commands, cancel all pending/queued downlink commands, and dispatch immediate high-priority valve shutdown downlinks.

#### Scenario: Emergency stop override trigger
- **WHEN** user or safety monitor issues an emergency stop command
- **THEN** system updates emergency stop state, cancels queued commands, dispatches valve close downlink, and rejects subsequent manual command requests until emergency stop is reset

