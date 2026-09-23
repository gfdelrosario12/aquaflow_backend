## Purpose

Manage edge devices including registration, status monitoring, heartbeat tracking, and lifecycle management for autonomous irrigation infrastructure.

## ADDED Requirements

### Requirement: System SHALL register edge devices
The system SHALL register new edge devices with unique device ID, hardware model, firmware version, and associated zones.

#### Scenario: Register new edge device
- **WHEN** a new edge device connects and provides registration details
- **THEN** system creates a device record and associates it with configured zones

### Requirement: System SHALL monitor device heartbeat
The system SHALL track device heartbeat signals and mark devices as offline after configurable timeout.

#### Scenario: Device heartbeat timeout
- **WHEN** device fails to send heartbeat within timeout period
- **THEN** system marks the device as offline and generates an alert

### Requirement: System SHALL track device status
The system SHALL maintain current status (online, offline, maintenance, error) for all registered devices.

#### Scenario: Query device status
- **WHEN** operations team queries device status
- **THEN** system returns current status with last seen timestamp

### Requirement: System SHALL manage device firmware
The system SHALL support firmware version tracking and target version assignment for over-the-air updates.

#### Scenario: Assign firmware update
- **WHEN** administrator assigns a firmware target version to a device group
- **THEN** system records the target version and schedules update rollout
