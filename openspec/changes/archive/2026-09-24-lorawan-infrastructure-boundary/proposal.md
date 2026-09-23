## Why

AquaFlow needs a provider-neutral LoRaWAN infrastructure boundary to decouple core domain services from specific Network Server implementations (such as ChirpStack or The Things Network). Introducing explicit SPI/API interfaces for uplink reception and downlink queuing allows seamless switching or multi-LNS deployment, webhook security validation, confirmed/unconfirmed downlink dispatch, correlation ID tracking, retries, and structured error handling without leaking provider-specific DTOs into the domain layer.

## What Changes

- **Provider-Neutral Infrastructure Interfaces**: Create `LoraNetworkServerClient` and `LoraWebhookHandler` interfaces in `com.aquaflow.backend.infrastructure.lora`.
- **ChirpStack Adapter Implementation**: Implement `ChirpStackNetworkServerAdapter` and `ChirpStackWebhookController` under `com.aquaflow.backend.infrastructure.lora.chirpstack` handling API tokens, webhook secret verification, DevEUI mapping, and downlink REST API dispatch (`POST /api/v1/thirdparty/chirpstack/webhook`).
- **Downlink Dispatch & Model Abstraction**: Define provider-neutral domain models `LoraDownlinkMessage`, `LoraDownlinkResult`, `DownlinkConfirmationMode`, and `LoraIntegrationException`.
- **Webhook Authentication & Security**: Enforce webhook secret header validation (`X-ChirpStack-Signature` or configured token) to reject unauthorized webhook invocations.
- **Resilience & Correlation ID Tracking**: Implement correlation ID propagation, exponential backoff retries, and timeout handling for LNS downlink REST API requests.

## Capabilities

### New Capabilities
- `lorawan-integration`: Provider-neutral LoRaWAN network server abstraction covering uplink webhook authentication, DevEUI resolution, confirmed/unconfirmed downlink dispatch, correlation ID propagation, and LNS error handling.

### Modified Capabilities
- `sensor-data-ingestion`: Add webhook security verification and provider-neutral LoRaWAN infrastructure adapter mapping.

## Impact

- **New Package & Classes**: `com.aquaflow.backend.infrastructure.lora` (sub-packages `chirpstack`, `dto`, `adapter`).
- **New REST Controller**: `com.aquaflow.backend.infrastructure.lora.chirpstack.ChirpStackWebhookController` mapped to `/api/v1/thirdparty/chirpstack/webhook`.
- **Domain Services**: `com.aquaflow.backend.domain.TelemetryIngestionService` and `com.aquaflow.backend.domain.EdgeNodeRegistryService` interact exclusively with provider-neutral models (`LoraDownlinkMessage`, `DecodedTelemetryPayload`).

