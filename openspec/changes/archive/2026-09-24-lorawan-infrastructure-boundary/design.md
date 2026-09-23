## Context

The AquaFlow backend requires a provider-neutral infrastructure boundary for LoRaWAN Network Server (LNS) integrations. To prevent ChirpStack or TTN DTO models from leaking into domain services (`TelemetryIngestionService` and `EdgeNodeRegistryService`), all LNS communication is placed behind clean SPI interfaces (`LoraNetworkServerClient` and `LoraWebhookHandler`) in `com.aquaflow.backend.infrastructure.lora`.

See `proposal.md` for background motivation.

## Goals / Non-Goals

**Goals:**
- Define provider-neutral domain models: `LoraDownlinkMessage`, `LoraDownlinkResult`, `DownlinkConfirmationMode`, and `LoraIntegrationException`.
- Define provider-neutral interfaces: `LoraNetworkServerClient` and `LoraWebhookHandler`.
- Implement `ChirpStackNetworkServerAdapter` and `ChirpStackWebhookController` under `com.aquaflow.backend.infrastructure.lora.chirpstack`.
- Enforce webhook secret header validation (`X-ChirpStack-Secret` or `Authorization` header check).
- Support confirmed and unconfirmed downlink queueing with correlation ID tracking.
- Handle LNS HTTP timeouts and retries cleanly.

**Non-Goals:**
- Direct gRPC server hosting (REST/HTTP webhook integration is standard and used).
- Storing unencrypted LoRaWAN root keys (AppKey/NwkKey) in backend persistence.

## Decisions

### Decision 1: Provider-Neutral Domain Abstraction (`LoraDownlinkMessage`)
- **Rationale**: Domain services generate downlinks containing target DevEUI, FPort, payload bytes, and confirmation mode without referencing ChirpStack-specific REST payload classes. The active `LoraNetworkServerClient` implementation translates this into provider-specific REST requests.
- **Alternatives Considered**: Passing raw ChirpStack JSON objects through domain services (rejected due to tight coupling and poor modularity).

### Decision 2: Infrastructure Package Isolation (`com.aquaflow.backend.infrastructure.lora`)
- **Rationale**: Keeping provider DTOs (`ChirpStackUplinkEvent`, `ChirpStackDownlinkRequest`, `ChirpStackDownlinkResponse`) inside `infrastructure.lora.chirpstack.dto` ensures clean separation and prevents vendor leakage.
- **Alternatives Considered**: Placing ChirpStack DTOs in global `com.aquaflow.backend.dto` (rejected due to domain pollution).

### Decision 3: Webhook Secret Header Security Guard
- **Rationale**: ChirpStack webhooks support custom header authorization (e.g. `X-ChirpStack-Secret`). The webhook controller validates this token against application properties (`aquaflow.lora.chirpstack.webhook-secret`) and returns 401 Unauthorized if invalid.
- **Alternatives Considered**: Unauthenticated webhook endpoint relying on IP whitelist (rejected due to security risks in multi-cloud deployments).

## Risks / Trade-offs

- **Risk**: LNS REST API unavailability during downlink dispatch.
  - **Mitigation**: `LoraNetworkServerClient` wraps HTTP calls with timeout controls, correlation ID logging, and throws structured `LoraIntegrationException`.

## Migration Plan

1. Create domain models (`LoraDownlinkMessage`, `LoraDownlinkResult`, `DownlinkConfirmationMode`) and exception (`LoraIntegrationException`).
2. Create SPI interfaces `LoraNetworkServerClient` and `LoraWebhookHandler`.
3. Create ChirpStack infrastructure DTOs (`ChirpStackUplinkEvent`, `ChirpStackDownlinkRequest`, `ChirpStackDownlinkResponse`).
4. Implement `ChirpStackNetworkServerAdapter` with REST client call support.
5. Implement `ChirpStackWebhookController` mapped to `POST /api/v1/thirdparty/chirpstack/webhook`.
6. Write unit tests for ChirpStack adapter and webhook controller.
7. Verify build and test suite execution with `mvn clean test`.

