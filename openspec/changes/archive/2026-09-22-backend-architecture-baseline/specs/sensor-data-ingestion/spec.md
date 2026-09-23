## Purpose

Ingest sensor data from edge devices via MQTT, validate readings, and persist for monitoring and analytics.

## ADDED Requirements

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
