## ADDED Requirements

### Requirement: System SHALL emit asynchronous telemetry system events on ingestion
The system SHALL publish a `TELEMETRY_RECEIVED` system event asynchronously via internal application event publisher after successfully persisting incoming telemetry readings, ensuring real-time notification without delaying HTTP/webhook ingestion responses.

#### Scenario: Publish telemetry received event after successful persistence
- **WHEN** telemetry readings are successfully persisted for an edge node during uplink processing
- **THEN** system publishes an asynchronous `TELEMETRY_RECEIVED` system event containing node ID, zone ID, reading counts, and timestamp

