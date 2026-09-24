## Context

See `proposal.md` for background and motivation. The backend currently receives manual irrigation requests, autonomous decisions, and emergency stops, but command state transitions across cloud acceptance, downlink delivery, edge acknowledgement, physical execution, and completion are not managed by a centralized, validated state machine.

## Goals / Non-Goals

**Goals:**
- Provide a robust, centralized state machine (`IrrigationCommandStateMachine`) that validates every state transition attempt.
- Support 9 command lifecycle states: `ACCEPTED`, `QUEUED`, `DOWNLINK_TRANSMITTED`, `EDGE_ACKNOWLEDGED`, `EXECUTING`, `COMPLETED`, `FAILED`, `CANCELLED`, `OVERRIDDEN`.
- Distinguish clearly between API acceptance, downlink network delivery, edge ACK, physical valve movement, and execution completion.
- Prevent invalid transitions (e.g., direct completion from `ACCEPTED` without execution, restarting terminal states, clearing emergency stops without proper operator authorization).
- Persist detailed, immutable transition history entries (`CommandStateTransitionHistory`).
- Expose REST API endpoints to inspect command state machine state and history.

**Non-Goals:**
- Replacing edge node local hardware safety interlocks (edge nodes maintain ultimate safety authority).
- Modifying underlying LoRaWAN physical layer protocols.

## Decisions

### Decision 1: Centralized State Machine Engine (`IrrigationCommandStateMachine`)
- **Choice**: Implement `IrrigationCommandStateMachine` with an explicit state transition matrix:
  - `ACCEPTED` → `QUEUED`, `CANCELLED`, `FAILED`
  - `QUEUED` → `DOWNLINK_TRANSMITTED`, `CANCELLED`, `FAILED`
  - `DOWNLINK_TRANSMITTED` → `EDGE_ACKNOWLEDGED`, `FAILED`, `CANCELLED`, `OVERRIDDEN`
  - `EDGE_ACKNOWLEDGED` → `EXECUTING`, `FAILED`, `CANCELLED`, `OVERRIDDEN`
  - `EXECUTING` → `COMPLETED`, `FAILED`, `CANCELLED`, `OVERRIDDEN`
  - `COMPLETED`, `FAILED`, `CANCELLED`, `OVERRIDDEN` → Terminal (no outgoing transitions allowed without explicit administrative re-authorization workflows)
- **Rationale**: Strict transition rules prevent illegal jumps (such as completing an unstarted command or silently overwriting failed commands).

### Decision 2: Entity & Transition History Schema
- **Choice**: Create `IrrigationCommandRecord` and `CommandStateTransitionHistory` JPA entities linked by correlation ID.
- **Fields**: `id`, `correlationId`, `commandType`, `targetFieldId`, `nodeId`, `currentState`, `actorSource`, `rationale`, `edgeAckMetrics`, `failureReason`, `createdAt`, `updatedAt`.
- **History Record**: `id`, `correlationId`, `fromState`, `toState`, `actorSource`, `transitionReason`, `metadataJson`, `timestamp`.

### Decision 3: Integration into Services
- **Choice**: Refactor `ManualControlService`, `EmergencyStopService`, and `AutonomousDecisionService` to delegate state updates to `IrrigationCommandStateMachine`.
- **Rationale**: Ensures uniform enforcement of transition rules across all command sources.

### Decision 4: Authorization Interlock for Emergency Stop Clearing
- **Choice**: Require explicit operator role check (`ROLE_ADMIN` / `ROLE_SUPERVISOR`) and non-empty resolution rationale when clearing or overriding an `EMERGENCY_STOP` command.

## Risks / Trade-offs

- [Risk] Asynchronous edge ACK out-of-order delivery due to LoRaWAN latency or retry logic.
  → **Mitigation**: State machine handles idempotency by checking command state before applying transitions; repeated ACKs for current state are treated as idempotent no-ops.
- [Risk] Increased database write overhead for transition history.
  → **Mitigation**: History entries are inserted asynchronously or in a lightweight transaction context without blocking edge telemetry ingestion pipelines.

