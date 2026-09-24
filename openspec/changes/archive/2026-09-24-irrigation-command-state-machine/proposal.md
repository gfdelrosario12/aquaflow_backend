## Why

Currently, AquaFlow command delivery and execution lack explicit state machine governance, state validation, and detailed transition metadata. When manual commands, autonomous irrigation decisions, downlink transmissions, or emergency stops are triggered, command lifecycle transitions (from cloud acceptance to edge delivery, physical execution, completion, or failure) are implicitly assumed or loosely tracked. This creates operational ambiguity, potential invalid transitions (e.g., completing an unstarted command, restarting completed commands, or clearing emergency stops without proper authorization), and insufficient auditability of physical edge execution versus cloud command acceptance.

## What Changes

- Implement explicit command and execution state machines governing autonomous irrigation decisions, manual commands, emergency stops, and downlink commands.
- Define formal lifecycle states: `ACCEPTED`, `QUEUED`, `DOWNLINK_TRANSMITTED`, `EDGE_ACKNOWLEDGED`, `EXECUTING`, `COMPLETED`, `FAILED`, `CANCELLED`, and `OVERRIDDEN`.
- Enforce state transition safety rules preventing invalid transitions (e.g., executing/completing unstarted commands, re-executing completed commands, clearing emergency stops without authorized credentials).
- Capture complete state transition context: transition timestamp, actor/source identity, correlation ID, failure reason, and edge acknowledgement details.
- Provide clear differentiation between cloud command acceptance, downlink delivery, edge node acknowledgement, physical valve execution, and final execution completion.
- Expose query APIs for command execution history, active command lifecycle states, and valid state transition options.

## Capabilities

### New Capabilities
- `irrigation-command-state-machine`: Defines command and execution state machines, lifecycle states, valid state transition rules, and transition history tracking for autonomous, manual, emergency, and downlink irrigation commands.

### Modified Capabilities
- `manual-emergency-irrigation-controls`: Enforces command state machine transitions and authorization rules on manual start/stop and emergency stop commands.
- `irrigation-api`: Exposes command state machine lifecycle status and transition history via REST endpoints.

## Impact

- Domain & Data Model: Introduces `CommandState`, `CommandTransitionReason`, `IrrigationCommandStateMachine`, and `CommandStateTransitionHistory` entities/DTOs.
- Services: Integrates state machine validation into `ManualControlService`, `EmergencyStopService`, and `AutonomousDecisionService`.
- Controllers & APIs: Updates manual, emergency, and decision API endpoints to expose full command lifecycle status and transition histories.
- Security & Safety: Enforces mandatory authorization checks on emergency stop resolution/clearing transitions.

