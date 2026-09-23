package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.DecodedTelemetryPayload;
import com.aquaflow.backend.dto.request.TelemetryUplinkRequest;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PayloadDecoderRegistry {

    private final List<PayloadDecoder> decoders;

    public PayloadDecoderRegistry(List<PayloadDecoder> decoders) {
        this.decoders = decoders;
    }

    public PayloadDecoder getDecoder(TelemetryUplinkRequest request) {
        return decoders.stream()
                .filter(d -> d.supports(request))
                .findFirst()
                .orElseThrow(() -> new ValidationException("UNSUPPORTED_PAYLOAD_FORMAT: No suitable decoder found for telemetry uplink"));
    }

    public DecodedTelemetryPayload decode(TelemetryUplinkRequest request) {
        return getDecoder(request).decode(request);
    }
}

