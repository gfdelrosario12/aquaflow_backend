package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.DecodedTelemetryPayload;
import com.aquaflow.backend.dto.request.TelemetryUplinkRequest;

public interface PayloadDecoder {
    boolean supports(TelemetryUplinkRequest request);
    DecodedTelemetryPayload decode(TelemetryUplinkRequest request);
}

