package com.aquaflow.backend.infrastructure.lora;

import com.aquaflow.backend.dto.response.TelemetryUplinkResponse;

public interface LoraWebhookHandler {
    boolean validateWebhookSecret(String secretHeader);
    TelemetryUplinkResponse processWebhookUplink(String secretHeader, Object rawPayload);
}

