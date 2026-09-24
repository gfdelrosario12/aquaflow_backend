# manual-emergency-irrigation-controls Specification

## Purpose
Provides operator fallback controls for manually starting and stopping irrigation, executing emergency stops, tracking command lifecycle states, and preserving edge safety interlocks.
## Requirements
### Requirement: System SHALL support operator manual irrigation start requests
The system SHALL validate and process manual irrigation start requests, requiring explicit operator authorization, target field ID, rationale, duration, and override semantics. Upon acceptance, the system SHALL create an audit log entry and queue an asynchronous LoRaWAN command downlink.

#### Scenario: Successfully request manual irrigation start
- **WHEN** an authorized operator submits a manual start request with a valid target field, rationale, and duration
- **THEN** the system generates an audit record, queues a LoRaWAN downlink, and returns a command lifecycle status of QUEUED with a unique correlation ID

#### Scenario: Reject manual irrigation start with invalid duration
- **WHEN** an operator submits a manual start request exceeding maximum allowed duration limits
- **THEN** the system rejects the request with a 400 Bad Request error and does not queue a downlink command

### Requirement: System SHALL support operator manual irrigation stop requests
The system SHALL process manual irrigation stop requests to immediately cancel active or pending manual irrigation cycles on a target field, creating an audit record and queueing a cancellation downlink.

#### Scenario: Successfully request manual irrigation stop
- **WHEN** an operator submits a manual stop request for an active field with valid operator ID and rationale
- **THEN** the system records an audit entry, queues a stop downlink, and returns status QUEUED

### Requirement: System SHALL execute highest-priority emergency stop fallback
The system SHALL execute emergency stop requests with highest processing priority, dispatching immediate shutdown downlinks to target fields or all edge nodes, raising audit log entries, and publishing emergency alarm events.

#### Scenario: Trigger emergency stop for a field
- **WHEN** an operator triggers an emergency stop for a target field
- **THEN** the system immediately queues emergency stop downlinks, logs an emergency audit record, publishes an EMERGENCY_STOP_ACTIVATED system event, and returns command status QUEUED

### Requirement: System SHALL track command lifecycle states without assuming immediate physical execution
The system SHALL track and enforce command execution state machine transitions including `ACCEPTED`, `QUEUED`, `DOWNLINK_TRANSMITTED`, `EDGE_ACKNOWLEDGED`, `EXECUTING`, `COMPLETED`, `FAILED`, `CANCELLED`, `OVERRIDDEN`, and `REJECTED_SAFETY_INTERLOCK`. HTTP API acceptance SHALL NOT imply physical execution on edge hardware.

#### Scenario: Process edge command status acknowledgement
- **WHEN** an edge node sends an acknowledgement or safety interlock rejection for a command correlation ID
- **THEN** the system updates the command status to `EDGE_ACKNOWLEDGED`, `EXECUTING`, `COMPLETED`, or `REJECTED_SAFETY_INTERLOCK` accordingly via valid state machine transitions

### Requirement: System SHALL preserve edge hardware safety interlocks for manual commands
The cloud backend SHALL pass hardware safety boundaries with manual commands and record edge safety interlock rejections without bypassing physical hardware protection on the edge.

#### Scenario: Edge node rejects manual start due to hardware safety limit
- **WHEN** an edge node rejects a manual command because a safety limit (such as maximum continuous open time or water depth limit) would be exceeded
- **THEN** the system records the rejection reason and sets the command lifecycle status to REJECTED_SAFETY_INTERLOCK

