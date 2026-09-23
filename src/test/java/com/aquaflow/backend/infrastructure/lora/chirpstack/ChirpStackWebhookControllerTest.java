package com.aquaflow.backend.infrastructure.lora.chirpstack;

import com.aquaflow.backend.domain.TelemetryIngestionService;
import com.aquaflow.backend.dto.request.TelemetryUplinkRequest;
import com.aquaflow.backend.dto.response.TelemetryUplinkResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ChirpStackWebhookControllerTest {

    @Mock
    private TelemetryIngestionService telemetryIngestionService;

    private ChirpStackWebhookController controller;
    private final String webhookSecret = "custom-secret-123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ChirpStackWebhookController(telemetryIngestionService, webhookSecret);
    }

    @Test
    void shouldValidateWebhookSecretCorrectly() {
        assertThat(controller.validateWebhookSecret("custom-secret-123")).isTrue();
        assertThat(controller.validateWebhookSecret("Bearer custom-secret-123")).isTrue();
        assertThat(controller.validateWebhookSecret("wrong-secret")).isFalse();
        assertThat(controller.validateWebhookSecret(null)).isFalse();
    }

    @Test
    void shouldAllowDefaultSecretWhenConfigured() {
        ChirpStackWebhookController defaultController = new ChirpStackWebhookController(telemetryIngestionService, "secret");
        assertThat(defaultController.validateWebhookSecret("anything")).isTrue();
        assertThat(defaultController.validateWebhookSecret(null)).isTrue();
    }

    @Test
    void shouldProcessUplinkWebhookSuccessfully() {
        Map<String, Object> mockPayload = Map.of("devEui", "0011223344556677", "data", "AQID");
        TelemetryUplinkResponse expectedResponse = TelemetryUplinkResponse.builder()
                .success(true)
                .nodeId("NODE-001")
                .message("Telemetry ingested successfully")
                .readingsIngested(2)
                .build();

        when(telemetryIngestionService.ingestUplink(any(TelemetryUplinkRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<TelemetryUplinkResponse> response = controller.handleUplinkWebhook(
                "custom-secret-123",
                null,
                "up",
                mockPayload
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getNodeId()).isEqualTo("NODE-001");
    }

    @Test
    void shouldRejectUnauthorizedWebhook() {
        Map<String, Object> mockPayload = Map.of("devEui", "0011223344556677");

        ResponseEntity<TelemetryUplinkResponse> response = controller.handleUplinkWebhook(
                "invalid-secret",
                null,
                "up",
                mockPayload
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).contains("UNAUTHORIZED_WEBHOOK");
    }

    @Test
    void shouldAcceptSecretFromAuthorizationHeader() {
        Map<String, Object> mockPayload = Map.of("devEui", "0011223344556677");
        TelemetryUplinkResponse expectedResponse = TelemetryUplinkResponse.builder()
                .success(true)
                .nodeId("NODE-001")
                .build();

        when(telemetryIngestionService.ingestUplink(any(TelemetryUplinkRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<TelemetryUplinkResponse> response = controller.handleUplinkWebhook(
                null,
                "Bearer custom-secret-123",
                "up",
                mockPayload
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().isSuccess()).isTrue();
    }
}

