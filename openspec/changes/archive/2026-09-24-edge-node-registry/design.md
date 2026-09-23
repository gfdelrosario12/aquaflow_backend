## Context

The AquaFlow database model includes the `EdgeNode` entity, `NodeLifecycleState`, `HealthState`, `CommunicationIdentity`, and `NodeHealthMetrics`. However, domain logic, lifecycle transition guards (`commission`, `decommission`, `replace`), transmission configuration, secure commissioning token generation, and dedicated REST controllers have not yet been implemented.

See `proposal.md` for the underlying business motivation.

## Goals / Non-Goals

**Goals:**
- Implement `EdgeNodeRegistryService` and `EdgeNodeRegistryServiceImpl` handling lifecycle transitions and health/telemetry queries.
- Build REST API controller `EdgeNodeRegistryController` mapped to `/api/v1/nodes`.
- Implement secure single-use commissioning token generation (e.g. SHA-256 token hash) returned only in `CommissionNodeResponse` during commissioning.
- Implement node replacement workflow: reassigning monitoring points from faulty node to replacement node, marking old node as `REPLACED`.
- Enforce lifecycle state invariants prohibiting unregistered, decommissioned, or replaced nodes from executing active operations.

**Non-Goals:**
- Direct hardware LoRaWAN Join-Accept packet processing (handled by external LoRa Network Server or MQTT gateway bridge).
- Storing unencrypted LoRaWAN root keys (AppKeys) in standard database fields.

## Decisions

### Decision 1: Single-Use Secure Commissioning Token Generation
- **Rationale**: When an edge node is commissioned via `POST /api/v1/nodes/{id}/commission`, a secure 256-bit entropy token is generated and returned once in `CommissionNodeResponse`. The hash is stored in memory/DB for initial device authentication. Standard DTO responses (`EdgeNodeResponse`) exclude this token to prevent credential exposure.
- **Alternatives Considered**: Static pre-shared keys in configuration files (rejected due to credential leakage risks across field hardware).

### Decision 2: Atomic Node Replacement Workflow (`replaceNode`)
- **Rationale**: When field operators replace a damaged physical node, all associated `MonitoringPoint` entities and `AutoIrrigationConfig` rules must be atomically transferred to the new node in a single `@Transactional` service call. The original node state transitions to `REPLACED`.
- **Alternatives Considered**: Manual multi-step client API calls to unbind and rebind points (rejected due to risks of partial state corruption during field operation).

### Decision 3: Audit Logging on Lifecycle Transitions
- **Rationale**: Every state transition (`PROVISIONED` → `COMMISSIONED` → `ACTIVE` → `DECOMMISSIONED` / `REPLACED`) persists an entry into `IrrigationAuditLog` with event type `NODE_LIFECYCLE_TRANSITION`, recording actor and payload.
- **Alternatives Considered**: Relying solely on database timestamp updates (rejected due to missing historical audit trail).

## Risks / Trade-offs

- **Risk**: Stale telemetry requests when a node is decommissioned while messages are in transit.
  - **Mitigation**: Ingest handlers query the node's `lifecycleState` before storing readings and reject requests if the node is not `ACTIVE` or `COMMISSIONED`.
- **Risk**: Accidental replacement of an active node with an un-provisioned hardware ID.
  - **Mitigation**: Validate that replacement node exists, is in `PROVISIONED` state, and does not already own active monitoring points before executing `replaceNode`.

## Migration Plan

1. Implement request and response DTOs (`RegisterNodeRequest`, `CommissionNodeRequest`, `CommissionNodeResponse`, `ReplaceNodeRequest`, `UpdateTransmissionRequest`, `NodeHealthResponse`).
2. Update `DtoMapper` to map `EdgeNode` health and transmission attributes cleanly.
3. Implement `EdgeNodeRegistryService` and `EdgeNodeRegistryServiceImpl` with state transition validation.
4. Implement `EdgeNodeRegistryController` with all 11 REST endpoints.
5. Write unit tests for service state transitions and controller endpoints.
6. Verify build and test suite execution with `mvn clean test`.

