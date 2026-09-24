## MODIFIED Requirements

### Requirement: System SHALL track command lifecycle states without assuming immediate physical execution
The system SHALL track and enforce command execution state machine transitions including `ACCEPTED`, `QUEUED`, `DOWNLINK_TRANSMITTED`, `EDGE_ACKNOWLEDGED`, `EXECUTING`, `COMPLETED`, `FAILED`, `CANCELLED`, `OVERRIDDEN`, and `REJECTED_SAFETY_INTERLOCK`. HTTP API acceptance SHALL NOT imply physical execution on edge hardware.

#### Scenario: Process edge command status acknowledgement
- **WHEN** an edge node sends an acknowledgement or safety interlock rejection for a command correlation ID
- **THEN** the system updates the command status to `EDGE_ACKNOWLEDGED`, `EXECUTING`, `COMPLETED`, or `REJECTED_SAFETY_INTERLOCK` accordingly via valid state machine transitions

