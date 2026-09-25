# irrigation-command-state-machine Specification

## Purpose

Manages the lifecycle, state machine transitions, safety interlocks, and state transition history for AquaFlow irrigation commands across autonomous, manual, emergency, and downlink command workflows.
## Requirements
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

### Requirement: System SHALL prevent invalid state transitions
The system SHALL validate all state transition attempts against valid transition rules and reject any invalid state transition.

#### Scenario: Reject completing unstarted command
- **WHEN** a client or event listener attempts to transition a command directly to `COMPLETED` while in `ACCEPTED` or `QUEUED` status without passing through `EXECUTING`
- **THEN** the system rejects the transition with a invalid state transition exception

#### Scenario: Reject restarting completed command
- **WHEN** a request attempts to transition a command in `COMPLETED`, `FAILED`, or `CANCELLED` status back to `EXECUTING` or `ACCEPTED`
- **THEN** the system rejects the request as an illegal state transition

#### Scenario: Reject unauthorized emergency stop resolution
- **WHEN** a request attempts to clear or transition an emergency stop command without explicit administrative operator authorization metadata
- **THEN** the system rejects the state transition and logs a security audit event

### Requirement: System SHALL persist state transition history and metadata
The system SHALL record an immutable transition history entry for every command state transition including timestamp, previous state, new state, actor identity, correlation ID, edge ACK payload, and failure reason when applicable.

#### Scenario: Record transition audit trail
- **WHEN** an irrigation command transitions between lifecycle states
- **THEN** the system appends a timestamped `CommandStateTransitionHistory` entry detailing actor source, correlation ID, previous state, resulting state, and transition metadata

