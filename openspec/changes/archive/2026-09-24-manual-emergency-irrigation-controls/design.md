## Context

The AquaFlow platform provides autonomous irrigation driven by edge node AWD policies. However, operators require fallback controls to manually start or stop irrigation and execute emergency stops when unexpected conditions occur or maintenance is required.

See `proposal.md` for overall motivation and requirements.

## Goals / Non-Goals

**Goals:**
- Provide `ManualControlService` to handle manual start and stop command requests with validation, audit logging, and LoRaWAN downlink dispatch.
- Provide `EmergencyStopService` to handle immediate, highest-priority emergency shutdown downlinks across target fields or all active nodes.
- Define DTOs: `ManualStartRequest`, `ManualStopRequest`, `EmergencyStopRequest`, and `CommandStatusResponse`.
- Expose REST API endpoints on `IrrigationController` (`/api/v1/irrigation/manual/start`, `/api/v1/irrigation/manual/stop`, `/api/v1/irrigation/emergency-stop`).
- Track command lifecycle states (`QUEUED`, `DELIVERED`, `ACKNOWLEDGED`, `EXECUTED`, `REJECTED_SAFETY_INTERLOCK`, `FAILED`).
- Retain edge hardware safety interlocks by including bounds validation parameters and recording edge safety interlock rejection feedback.

**Non-Goals:**
- Recalculating or overriding AWD autonomous algorithms in the cloud during normal operation.
- Executing direct hardware pin mutations from the cloud API thread; all commands are queued asynchronously via LoRaWAN downlink infrastructure.

## Decisions

### Decision 1: Command Lifecycle State Management
- **Choice**: Introduce explicit command lifecycle tracking (`QUEUED`, `DELIVERED`, `ACKNOWLEDGED`, `EXECUTED`, `REJECTED_SAFETY_INTERLOCK`, `FAILED`) stored with correlation IDs.
- **Rationale**: HTTP API acceptance returns `QUEUED` immediately. Physical execution status depends on LoRaWAN transmission and edge acknowledgement.
- **Alternatives Considered**: Synchronous HTTP waiting for edge ack (rejected: LoRaWAN downlink delays and duty cycle limits make sync requests impractical).

### Decision 2: Emergency Stop Priority & Fan-Out
- **Choice**: `EmergencyStopService` dispatches downlinks with priority `HIGH`/`EMERGENCY`, bypasses normal queue delays, logs audit records, and emits system events (`EMERGENCY_STOP_ACTIVATED`, `ALARM_TRIGGERED`).
- **Rationale**: Emergency stops must preempt standard telemetry and configuration downlinks.
- **Alternatives Considered**: Regular queueing (rejected: risks delayed shutdown during physical hazard).

### Decision 3: Preserving Edge Safety Interlocks
- **Choice**: Cloud manual start commands enforce validation rules (e.g. max duration upper bound, target field existence) and attach hardware safety boundaries in the payload. If the edge device determines the command violates a local physical safety rule, it acknowledges with `REJECTED_SAFETY_INTERLOCK`.
- **Rationale**: Hardware safety interlocks on edge microcontrollers protect pumps, valves, and field flooding limits even if an operator sends an out-of-bounds command.
- **Alternatives Considered**: Total cloud override that bypasses edge hardware limits (rejected: dangerous to physical field infrastructure).

## Risks / Trade-offs

- **[Risk]** LoRaWAN downlink latency or packet loss may delay manual command execution.
  - **Mitigation**: Immediate return of `QUEUED` command correlation ID, retries via `DownlinkQueueService`, and clear status reporting APIs.
- **[Risk]** Concurrent emergency stop and manual start requests for the same field.
  - **Mitigation**: Emergency stop has absolute priority and clears active manual commands for the field.

