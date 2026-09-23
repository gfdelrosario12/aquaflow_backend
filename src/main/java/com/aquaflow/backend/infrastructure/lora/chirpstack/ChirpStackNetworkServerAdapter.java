package com.aquaflow.backend.infrastructure.lora.chirpstack;

import com.aquaflow.backend.infrastructure.lora.LoraNetworkServerClient;
import com.aquaflow.backend.infrastructure.lora.chirpstack.dto.ChirpStackDownlinkRequest;
import com.aquaflow.backend.infrastructure.lora.chirpstack.dto.ChirpStackDownlinkResponse;
import com.aquaflow.backend.infrastructure.lora.exception.LoraIntegrationException;
import com.aquaflow.backend.infrastructure.lora.model.DownlinkConfirmationMode;
import com.aquaflow.backend.infrastructure.lora.model.LoraDownlinkMessage;
import com.aquaflow.backend.infrastructure.lora.model.LoraDownlinkResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@Component
public class ChirpStackNetworkServerAdapter implements LoraNetworkServerClient {

    private static final Logger log = LoggerFactory.getLogger(ChirpStackNetworkServerAdapter.class);

    @Value("${aquaflow.lora.chirpstack.api-url:http://localhost:8080}")
    private String apiUrl;

    @Value("${aquaflow.lora.chirpstack.api-token:}")
    private String apiToken;

    private final RestTemplate restTemplate;

    public ChirpStackNetworkServerAdapter() {
        this.restTemplate = new RestTemplate();
    }

    public ChirpStackNetworkServerAdapter(RestTemplate restTemplate, String apiUrl, String apiToken) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
    }

    @Override
    public boolean supports(String providerName) {
        return "CHIRPSTACK".equalsIgnoreCase(providerName) || providerName == null;
    }

    @Override
    public LoraDownlinkResult sendDownlink(LoraDownlinkMessage message) {
        if (message == null || message.getDevEui() == null || message.getDevEui().isBlank()) {
            throw new LoraIntegrationException("INVALID_DOWNLINK_REQUEST: DevEUI is required for downlink dispatch", 400, "INVALID_DEVEUI", null);
        }

        String correlationId = message.getCorrelationId() != null ? message.getCorrelationId() : UUID.randomUUID().toString();
        log.info("Sending ChirpStack downlink for DevEUI: {} (CorrelationID: {})", message.getDevEui(), correlationId);

        String base64Data = resolveBase64Payload(message);
        boolean confirmed = message.getConfirmationMode() == DownlinkConfirmationMode.CONFIRMED;
        int port = message.getFPort() != null ? message.getFPort() : 10;

        ChirpStackDownlinkRequest.QueueItem queueItem = ChirpStackDownlinkRequest.QueueItem.builder()
                .devEui(message.getDevEui())
                .confirmed(confirmed)
                .fPort(port)
                .data(base64Data)
                .build();

        ChirpStackDownlinkRequest request = ChirpStackDownlinkRequest.builder()
                .queueItem(queueItem)
                .build();

        String url = apiUrl + "/api/devices/" + message.getDevEui() + "/queue";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Correlation-ID", correlationId);
        if (apiToken != null && !apiToken.isBlank()) {
            headers.set("Authorization", "Bearer " + apiToken);
        }

        HttpEntity<ChirpStackDownlinkRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<ChirpStackDownlinkResponse> response = restTemplate.postForEntity(url, entity, ChirpStackDownlinkResponse.class);
            ChirpStackDownlinkResponse body = response.getBody();

            String id = body != null && body.getId() != null ? body.getId() : UUID.randomUUID().toString();
            Long fCnt = body != null ? body.getFCnt() : null;

            return LoraDownlinkResult.builder()
                    .success(true)
                    .devEui(message.getDevEui())
                    .downlinkId(id)
                    .correlationId(correlationId)
                    .message("Downlink queued successfully with ChirpStack")
                    .fCnt(fCnt)
                    .timestamp(LocalDateTime.now())
                    .build();

        } catch (HttpStatusCodeException e) {
            log.error("ChirpStack API returned error {}: {}", e.getStatusCode().value(), e.getResponseBodyAsString());
            throw new LoraIntegrationException(
                    "CHIRPSTACK_DOWNLINK_FAILED: HTTP " + e.getStatusCode().value() + " - " + e.getResponseBodyAsString(),
                    e.getStatusCode().value(),
                    "LNS_HTTP_ERROR",
                    message.getDevEui()
            );
        } catch (Exception e) {
            log.error("Failed to connect to ChirpStack API: {}", e.getMessage());
            throw new LoraIntegrationException(
                    "CHIRPSTACK_CONNECTION_ERROR: " + e.getMessage(),
                    500,
                    "LNS_CONNECTION_ERROR",
                    message.getDevEui()
            );
        }
    }

    private String resolveBase64Payload(LoraDownlinkMessage message) {
        if (message.getPayloadBase64() != null && !message.getPayloadBase64().isBlank()) {
            return message.getPayloadBase64();
        }
        if (message.getPayloadHex() != null && !message.getPayloadHex().isBlank()) {
            byte[] bytes = HexFormat.of().parseHex(message.getPayloadHex());
            return Base64.getEncoder().encodeToString(bytes);
        }
        return Base64.getEncoder().encodeToString("NOP".getBytes(StandardCharsets.UTF_8));
    }
}

