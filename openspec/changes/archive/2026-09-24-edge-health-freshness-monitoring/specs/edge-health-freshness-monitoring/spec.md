## Purpose

Evaluates edge node health, telemetry freshness, battery levels, signal quality, and communication failures while publishing realtime health events and preserving edge node local autonomy.

## ADDED Requirements

### Requirement: System SHALL automatically evaluate edge node health states
The system SHALL periodically evaluate edge node metrics against configured health thresholds and transition node state to HEALTHY, DEGRADED, CRITICAL, or OFFLINE based on battery level, signal quality, communication failures, and config sync status.

#### Scenario: Transition node to DEGRADED on low battery or signal
- **WHEN** an edge node reports battery percentage below 20% or RSSI below -115 dBm
- **THEN** the system updates the node health status to DEGRADED and logs the health metric update

#### Scenario: Transition node to OFFLINE when reporting exceeds timeout limit
- **WHEN** an edge node has not communicated for longer than the offline threshold (e.g., 60 minutes)
- **THEN** the system sets the node state to OFFLINE and triggers a NODE_STATUS_CHANGED event

### Requirement: System SHALL detect telemetry staleness
The system SHALL monitor telemetry reading timestamps for each registered edge node and flag telemetry data as STALE when the age of the latest reading exceeds the configured freshness threshold.

#### Scenario: Flag stale telemetry readings
- **WHEN** the last received telemetry timestamp for a node is older than 30 minutes
- **THEN** the system flags the telemetry status as STALE and publishes an ALARM_TRIGGERED event

### Requirement: System SHALL persist health state transitions and publish events
The system SHALL record significant node health state transitions and publish realtime system events for consumption by web application subscribers.

#### Scenario: Publish node status change event on transition
- **WHEN** a node transitions from HEALTHY to CRITICAL or OFFLINE
- **THEN** the system persists the new health state and publishes a NODE_STATUS_CHANGED event containing old and new state details

### Requirement: System SHALL preserve edge node local autonomy during offline or critical states
The cloud backend SHALL NOT interpret an offline or degraded node state as authorization to override local edge AWD policy execution or disable physical edge hardware safety interlocks.

#### Scenario: Node marked offline retains local irrigation control
- **WHEN** a node state is set to OFFLINE in the cloud backend
- **THEN** the cloud backend maintains policy data boundaries without attempting direct hardware valve override, preserving edge safety interlocks

