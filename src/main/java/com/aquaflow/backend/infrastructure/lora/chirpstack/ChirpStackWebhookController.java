package com.aquaflow.backend.infrastructure.lora.chirpstack;

import com.aquaflow.backend.domain.TelemetryIngestionService;
import com.aquaflow.backend.dto.request.TelemetryUplinkRequest;
import com.aquaflow.backend.dto.response.TelemetryUplinkResponse;
import com.aquaflow.backend.infrastructure.lora.LoraWebhookHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/thirdparty/chirpstack/webhook")
public class ChirpStackWebhookController implements LoraWebhookHandler {

    private static final Logger log = LoggerFactory.getLogger(ChirpStackWebhookController.class);

    @Value("${aquaflow.lora.chirpstack.webhook-secret:secret}")
    private String configuredSecret;

    private final TelemetryIngestionService telemetryIngestionService;

    @Autowired
    public ChirpStackWebhookController(TelemetryIngestionService telemetryIngestionService) {
        this.telemetryIngestionService = telemetryIngestionService;
    }

    public ChirpStackWebhookController(TelemetryIngestionService telemetryIngestionService, String configuredSecret) {
        this.telemetryIngestionService = telemetryIngestionService;
        this.configuredSecret = configuredSecret;
    }

    @Override
    public boolean validateWebhookSecret(String secretHeader) {
        if (configuredSecret == null || configuredSecret.isBlank() || "secret".equalsIgnoreCase(configuredSecret)) {
            return true; // Secret validation disabled or default
        }
        if (secretHeader == null) {
            return false;
        }
        String cleanHeader = secretHeader.startsWith("Bearer ") ? secretHeader.substring(7) : secretHeader;
        return configuredSecret.equals(cleanHeader);
    }

    @Override
    public TelemetryUplinkResponse processWebhookUplink(String secretHeader, Object rawPayload) {
        if (!validateWebhookSecret(secretHeader)) {
            return TelemetryUplinkResponse.builder()
                    .success(false)
                    .message("UNAUTHORIZED_WEBHOOK: Invalid ChirpStack webhook secret")
                    .build();
        }

        TelemetryUplinkRequest request = TelemetryUplinkRequest.builder()
                .format("CHIRPSTACK")
                .payload(rawPayload)
                .build();

        return telemetryIngestionService.ingestUplink(request);
    }

    @PostMapping
    public ResponseEntity<TelemetryUplinkResponse> handleUplinkWebhook(
            @RequestHeader(value = "X-ChirpStack-Secret", required = false) String secretHeader,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "event", required = false, defaultValue = "up") String event,
            @RequestBody Object rawPayload) {

        log.info("Received ChirpStack event webhook: {}", event);

        String token = secretHeader != null ? secretHeader : authHeader;
        if (!validateWebhookSecret(token)) {
            log.warn("Unauthorized ChirpStack webhook access attempt");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(TelemetryUplinkResponse.builder()
                            .success(false)
                            .message("UNAUTHORIZED_WEBHOOK: Invalid ChirpStack webhook secret")
                            .build());
        }

        TelemetryUplinkResponse response = processWebhookUplink(token, rawPayload);
        return ResponseEntity.ok(response);
    }
}

