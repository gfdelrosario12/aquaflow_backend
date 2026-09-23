## 1. Provider-Neutral Domain Models & Interfaces

- [x] 1.1 Create `DownlinkConfirmationMode` enum and `LoraDownlinkMessage` model in `com.aquaflow.backend.infrastructure.lora`
- [x] 1.2 Create `LoraDownlinkResult` response model and `LoraIntegrationException` exception
- [x] 1.3 Create provider-neutral SPI interfaces `LoraNetworkServerClient` and `LoraWebhookHandler`

## 2. ChirpStack Infrastructure DTOs and Adapter Implementation

- [x] 2.1 Create ChirpStack DTOs (`ChirpStackUplinkEvent`, `ChirpStackDownlinkRequest`, `ChirpStackDownlinkResponse`) in `com.aquaflow.backend.infrastructure.lora.chirpstack.dto`
- [x] 2.2 Implement `ChirpStackNetworkServerAdapter` in `com.aquaflow.backend.infrastructure.lora.chirpstack` implementing `LoraNetworkServerClient`
- [x] 2.3 Add correlation ID tracking, timeout handling, and HTTP error transformation in `ChirpStackNetworkServerAdapter`

## 3. ChirpStack Webhook Controller & Security Validation

- [x] 3.1 Create `ChirpStackWebhookController` in `com.aquaflow.backend.infrastructure.lora.chirpstack` mapped to `/api/v1/thirdparty/chirpstack/webhook`
- [x] 3.2 Implement webhook authentication header verification (`X-ChirpStack-Secret` or authorization token check)
- [x] 3.3 Map ChirpStack uplink events to `TelemetryUplinkRequest` and delegate to `TelemetryIngestionService`

## 4. Verification and Integration Testing

- [x] 4.1 Write unit tests for `ChirpStackNetworkServerAdapter` covering downlink dispatch, confirmed/unconfirmed modes, and LNS error handling
- [x] 4.2 Write unit/WebMvc tests for `ChirpStackWebhookController` covering valid authentication, unauthorized header rejection, and payload normalization
- [x] 4.3 Run `mvn clean compile` to verify clean compilation
- [x] 4.4 Run `mvn test` to verify all unit and integration tests pass


