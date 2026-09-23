package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.DecodedTelemetryPayload;
import com.aquaflow.backend.dto.SignalMetadataDto;
import com.aquaflow.backend.dto.request.TelemetryUplinkRequest;
import com.aquaflow.backend.entity.SensorType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class TtnPayloadDecoder implements PayloadDecoder {

    private final ObjectMapper objectMapper;

    public TtnPayloadDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(TelemetryUplinkRequest request) {
        if (request == null) return false;
        if ("TTN".equalsIgnoreCase(request.getFormat())) {
            return true;
        }
        if (request.getPayload() != null) {
            String json = request.getPayload().toString();
            return json.contains("end_device_ids") || json.contains("uplink_message");
        }
        return false;
    }

    @Override
    public DecodedTelemetryPayload decode(TelemetryUplinkRequest request) {
        DecodedTelemetryPayload payload = new DecodedTelemetryPayload();
        payload.setNodeId(request.getNodeId());
        payload.setDevEui(request.getDevEui());
        payload.setFCnt(request.getFCnt());
        payload.setFPort(request.getFPort());
        payload.setTimestamp(request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now());
        payload.setBatteryLevel(request.getBatteryLevel());
        payload.setSolarVoltage(request.getSolarVoltage());

        SignalMetadataDto signal = SignalMetadataDto.builder()
                .rssiDbm(request.getRssiDbm())
                .snrDb(request.getSnrDb())
                .gatewayId(request.getGatewayId())
                .frequency(request.getFrequency())
                .build();
        payload.setSignalMetadata(signal);

        Map<SensorType, Double> measurements = new HashMap<>();

        if (request.getPayload() != null) {
            try {
                JsonNode root = request.getPayload() instanceof JsonNode ? (JsonNode) request.getPayload()
                        : objectMapper.readTree(request.getPayload().toString());

                if (root.has("end_device_ids") && root.get("end_device_ids").has("dev_eui")) {
                    payload.setDevEui(root.get("end_device_ids").get("dev_eui").asText());
                }

                if (root.has("uplink_message")) {
                    JsonNode uplink = root.get("uplink_message");
                    if (uplink.has("f_cnt")) {
                        payload.setFCnt(uplink.get("f_cnt").asLong());
                    }
                    if (uplink.has("f_port")) {
                        payload.setFPort(uplink.get("f_port").asInt());
                    }
                    if (uplink.has("decoded_payload")) {
                        parseMeasurements(uplink.get("decoded_payload"), measurements, payload);
                    }
                    if (uplink.has("rx_metadata") && uplink.get("rx_metadata").isArray() && uplink.get("rx_metadata").size() > 0) {
                        JsonNode rx = uplink.get("rx_metadata").get(0);
                        if (rx.has("rssi")) signal.setRssiDbm(rx.get("rssi").asInt());
                        if (rx.has("snr")) signal.setSnrDb(rx.get("snr").asDouble());
                    }
                }
            } catch (Exception ignored) {}
        }

        if (request.getMeasurements() != null) {
            request.getMeasurements().forEach((k, v) -> {
                try {
                    SensorType type = SensorType.valueOf(k.toUpperCase());
                    measurements.put(type, v);
                } catch (IllegalArgumentException ignored) {}
            });
        }

        payload.setMeasurements(measurements);
        return payload;
    }

    private void parseMeasurements(JsonNode node, Map<SensorType, Double> measurements, DecodedTelemetryPayload payload) {
        if (node.has("soil_moisture") || node.has("soilMoisture")) {
            double v = node.has("soil_moisture") ? node.get("soil_moisture").asDouble() : node.get("soilMoisture").asDouble();
            measurements.put(SensorType.SOIL_MOISTURE, v);
        }
        if (node.has("temperature")) {
            measurements.put(SensorType.TEMPERATURE, node.get("temperature").asDouble());
        }
        if (node.has("humidity")) {
            measurements.put(SensorType.HUMIDITY, node.get("humidity").asDouble());
        }
        if (node.has("battery")) {
            payload.setBatteryLevel(node.get("battery").asDouble());
        }
        if (node.has("solar")) {
            payload.setSolarVoltage(node.get("solar").asDouble());
        }
    }
}

