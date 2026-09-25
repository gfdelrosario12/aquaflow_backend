## MODIFIED Requirements

### Requirement: System SHALL model explicit irrigation command lifecycle state machine
The system SHALL evaluate and enforce command execution lifecycle states (`ACCEPTED`, `QUEUED`, `DOWNLINK_TRANSMITTED`, `EDGE_ACKNOWLEDGED`, `EXECUTING`, `COMPLETED`, `FAILED`, `CANCELLED`, `OVERRIDDEN`) across manual commands, emergency stops, and downlink commands while strictly requiring edge nodes to drive autonomous valve decisions.

#### Scenario: Cloud command acceptance
- **WHEN** an irrigation command is accepted by the cloud backend API
- **THEN** the system transitions the command to `ACCEPTED` status and records initial command metadata, correlation ID, and actor source

#### Scenario: Downlink delivery transmission
- **WHEN** a command payload is transmitted to the LoRaWAN network server or edge gateway
- **THEN** the system transitions the command state from `ACCEPTED` or `QUEUED` to `DOWNLINK_TRANSMITTED`

#### Scenario: Edge node acknowledgment
- **WHEN** an edge node acknowledges reception of a downlink command via uplink ACK frame
- **THEN** the system updates the command state to `EDGE_ACKNOWLEDGED` with edge timestamp and signal metrics

#### Scenario: Physical valve execution
- **WHEN** edge node telemetry confirms physical valve operation has commenced
- **THEN** the system transitions the command state to `EXECUTING`

#### Scenario: Command execution completion
- **WHEN** edge node telemetry confirms target duration or target water level is reached and valve returns to baseline position
- **THEN** the system transitions the command state to `COMPLETED`
