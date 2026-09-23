package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.DecodedTelemetryPayload;
import com.aquaflow.backend.dto.SignalMetadataDto;
import com.aquaflow.backend.dto.request.TelemetryUplinkRequest;
import com.aquaflow.backend.entity.SensorType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonPayloadDecoder implements PayloadDecoder {

    @Override
    public boolean supports(TelemetryUplinkRequest request) {
        // Fallback decoder supporting generic JSON / direct HTTP requests
        return true;
    }

    @Override
    public DecodedTelemetryPayload decode(TelemetryUplinkRequest request) {
        DecodedTelemetryPayload payload = new DecodedTelemetryPayload();
        payload.setNodeId(request.getNodeId());
        payload.setDevEui(request.getDevEui());
        payload.setFCnt(request.getFCnt());
        payload.setFPort(request.getFPort());
        payload.setTimestamp(request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now());
        payload.setBatteryLevel(request.getBatteryLevel() != null ? request.getBatteryLevel() : 100.0);
        payload.setSolarVoltage(request.getSolarVoltage() != null ? request.getSolarVoltage() : 5.0);

        SignalMetadataDto signal = SignalMetadataDto.builder()
                .rssiDbm(request.getRssiDbm() != null ? request.getRssiDbm() : -60)
                .snrDb(request.getSnrDb() != null ? request.getSnrDb() : 10.0)
                .gatewayId(request.getGatewayId())
                .frequency(request.getFrequency())
                .build();
        payload.setSignalMetadata(signal);

        Map<SensorType, Double> measurements = new HashMap<>();

        if (request.getMeasurements() != null) {
            request.getMeasurements().forEach((key, value) -> {
                try {
                    SensorType type = SensorType.valueOf(key.toUpperCase());
                    measurements.put(type, value);
                } catch (IllegalArgumentException ignored) {}
            });
        }

        if (measurements.isEmpty()) {
            measurements.put(SensorType.SOIL_MOISTURE, 35.0);
        }

        payload.setMeasurements(measurements);
        return payload;
    }
}

