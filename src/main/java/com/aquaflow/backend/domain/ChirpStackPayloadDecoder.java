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
public class ChirpStackPayloadDecoder implements PayloadDecoder {

    private final ObjectMapper objectMapper;

    public ChirpStackPayloadDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(TelemetryUplinkRequest request) {
        if (request == null) return false;
        if ("CHIRPSTACK".equalsIgnoreCase(request.getFormat())) {
            return true;
        }
        if (request.getPayload() != null) {
            String json = request.getPayload().toString();
            return json.contains("deviceInfo") || json.contains("devEui") || json.contains("deduplicationId");
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

                if (root.has("deviceInfo") && root.get("deviceInfo").has("devEui")) {
                    payload.setDevEui(root.get("deviceInfo").get("devEui").asText());
                } else if (root.has("devEui")) {
                    payload.setDevEui(root.get("devEui").asText());
                }

                if (root.has("fCnt")) {
                    payload.setFCnt(root.get("fCnt").asLong());
                }

                if (root.has("fPort")) {
                    payload.setFPort(root.get("fPort").asInt());
                }

                JsonNode objectNode = root.has("object") ? root.get("object") : (root.has("objectJson") ? root.get("objectJson") : null);
                if (objectNode != null) {
                    parseMeasurementsFromNode(objectNode, measurements, payload);
                }

                if (root.has("rxInfo") && root.get("rxInfo").isArray() && root.get("rxInfo").size() > 0) {
                    JsonNode rx = root.get("rxInfo").get(0);
                    if (rx.has("rssi")) signal.setRssiDbm(rx.get("rssi").asInt());
                    if (rx.has("snr")) signal.setSnrDb(rx.get("snr").asDouble());
                    if (rx.has("gatewayId")) signal.setGatewayId(rx.get("gatewayId").asText());
                }
            } catch (Exception e) {
                // If parsing raw JSON fails, fallback to direct measurements map
            }
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

    private void parseMeasurementsFromNode(JsonNode node, Map<SensorType, Double> measurements, DecodedTelemetryPayload payload) {
        if (node.has("soilMoisture") || node.has("soil_moisture")) {
            double val = node.has("soilMoisture") ? node.get("soilMoisture").asDouble() : node.get("soil_moisture").asDouble();
            measurements.put(SensorType.SOIL_MOISTURE, val);
        }
        if (node.has("temperature")) {
            measurements.put(SensorType.TEMPERATURE, node.get("temperature").asDouble());
        }
        if (node.has("humidity")) {
            measurements.put(SensorType.HUMIDITY, node.get("humidity").asDouble());
        }
        if (node.has("battery") || node.has("batteryLevel")) {
            double b = node.has("battery") ? node.get("battery").asDouble() : node.get("batteryLevel").asDouble();
            payload.setBatteryLevel(b);
        }
        if (node.has("solarVoltage") || node.has("solar_voltage")) {
            double s = node.has("solarVoltage") ? node.get("solarVoltage").asDouble() : node.get("solar_voltage").asDouble();
            payload.setSolarVoltage(s);
        }
    }
}

