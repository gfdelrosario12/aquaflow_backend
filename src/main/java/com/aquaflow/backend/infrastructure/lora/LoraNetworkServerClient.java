package com.aquaflow.backend.infrastructure.lora;

import com.aquaflow.backend.infrastructure.lora.model.LoraDownlinkMessage;
import com.aquaflow.backend.infrastructure.lora.model.LoraDownlinkResult;

public interface LoraNetworkServerClient {
    boolean supports(String providerName);
    LoraDownlinkResult sendDownlink(LoraDownlinkMessage message);
}

