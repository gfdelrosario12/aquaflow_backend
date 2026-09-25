## ADDED Requirements

### Requirement: System SHALL guarantee edge node autonomous irrigation decision sovereignty
The system SHALL treat the edge node as the sole autonomous authority for production irrigation execution, prohibiting cloud override or cloud takeover when an edge node is offline or degraded.

#### Scenario: Offline node maintains edge control
- **WHEN** an edge node health status transitions to OFFLINE or DEGRADED
- **THEN** the cloud backend maintains telemetry boundaries without attempting direct hardware valve override or generating cloud-based autonomous irrigation triggers

