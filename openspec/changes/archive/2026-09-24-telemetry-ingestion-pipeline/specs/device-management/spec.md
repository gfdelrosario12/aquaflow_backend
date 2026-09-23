## ADDED Requirements

### Requirement: System SHALL update EdgeNode status and health metrics upon telemetry ingestion
The system SHALL automatically update `EdgeNode` last-seen heartbeat timestamp, battery level, solar voltage, and signal strength (RSSI/SNR) upon successful telemetry ingestion.

#### Scenario: Update node heartbeat and battery metrics on valid telemetry
- **WHEN** telemetry ingestion pipeline processes a valid uplink containing battery voltage, solar voltage, and signal metrics
- **THEN** system updates the corresponding `EdgeNode` `healthMetrics` and refreshes `lastHeartbeat` to current timestamp

#### Scenario: Maintain node health state based on signal and battery threshold
- **WHEN** telemetry payload reports battery level below critical threshold or severely degraded signal RSSI
- **THEN** system updates node `healthState` to `DEGRADED` or `CRITICAL` according to operational parameters

