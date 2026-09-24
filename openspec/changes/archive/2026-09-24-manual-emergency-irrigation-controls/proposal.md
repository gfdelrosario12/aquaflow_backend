## Why

Field operators require reliable fallback mechanisms to manually start/stop irrigation or perform emergency stops when automated AWD policies require operator intervention or unsafe physical conditions arise. Cloud control commands must be dispatched asynchronously to edge nodes over LoRaWAN, expose clear lifecycle states, preserve physical edge safety interlocks, and create immutable audit logs.

## What Changes

- Implement `ManualControlService` to manage operator-initiated manual irrigation start and stop actions.
- Implement `EmergencyStopService` to issue highest-priority emergency stop commands to specific fields or edge nodes.
- Expose REST endpoints:
  - `POST /api/v1/irrigation/manual/start` to request manual irrigation with operator ID, rationale, target field, duration, and override options.
  - `POST /api/v1/irrigation/manual/stop` to request immediate cancellation of manual irrigation.
  - `POST /api/v1/irrigation/emergency-stop` to trigger emergency shutdown across targeted or all fields.
- Integrate with `DownlinkQueueService` to queue command downlinks asynchronously with correlation IDs and tracked lifecycle states (`QUEUED`, `DELIVERED`, `ACKNOWLEDGED`, `EXECUTED`, `REJECTED_SAFETY_INTERLOCK`, `FAILED`).
- Integrate with `SystemEventPublisher` to emit audit and alert events (`ALARM_TRIGGERED`, `EMERGENCY_STOP_ACTIVATED`, `IRRIGATION_DECISION_MADE`).
- Store comprehensive audit records in `IrrigationAuditLog`.
- Ensure edge nodes preserve hardware safety interlocks, rejecting manual cloud commands if safety limits (e.g. max continuous run time, water bounds) would be violated.

## Capabilities

### New Capabilities
- `manual-emergency-irrigation-controls`: Operator fallback mechanisms for manual irrigation control and emergency stop execution, including audit trail recording, asynchronous command tracking, and edge safety interlock preservation.

### Modified Capabilities
- `irrigation-api`: Add REST endpoints for manual start, manual stop, and emergency stop operations under `/api/v1/irrigation/`.

## Impact

- Domain & Services: `ManualControlService`, `EmergencyStopService`, `DownlinkQueueService`, `SystemEventPublisher`, `IrrigationAuditLogRepository`.
- Web / Controllers: `IrrigationController` updated with manual and emergency endpoints and DTOs.
- DTOs: `ManualStartRequest`, `ManualStopRequest`, `EmergencyStopRequest`, `CommandStatusResponse`.
- Infrastructure: Downlink queue payload generation for manual and emergency LoRaWAN commands.

