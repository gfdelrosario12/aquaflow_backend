package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.DecodedTelemetryPayload;
import com.aquaflow.backend.dto.request.TelemetryUplinkRequest;
import com.aquaflow.backend.entity.SensorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PayloadDecoderTest {

    private ObjectMapper objectMapper;
    private ChirpStackPayloadDecoder chirpStackDecoder;
    private TtnPayloadDecoder ttnDecoder;
    private JsonPayloadDecoder jsonDecoder;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        chirpStackDecoder = new ChirpStackPayloadDecoder(objectMapper);
        ttnDecoder = new TtnPayloadDecoder(objectMapper);
        jsonDecoder = new JsonPayloadDecoder();
    }

    @Test
    void chirpStackDecoderShouldSupportAndDecodeChirpStackFormat() {
        TelemetryUplinkRequest request = TelemetryUplinkRequest.builder()
                .format("CHIRPSTACK")
                .nodeId("NODE-001")
                .devEui("0004A30B001F1234")
                .fCnt(100L)
                .batteryLevel(90.0)
                .solarVoltage(5.2)
                .measurements(Map.of("SOIL_MOISTURE", 42.5))
                .build();

        assertThat(chirpStackDecoder.supports(request)).isTrue();
        DecodedTelemetryPayload decoded = chirpStackDecoder.decode(request);

        assertThat(decoded).isNotNull();
        assertThat(decoded.getFCnt()).isEqualTo(100L);
        assertThat(decoded.getMeasurements()).containsEntry(SensorType.SOIL_MOISTURE, 42.5);
    }

    @Test
    void ttnDecoderShouldSupportAndDecodeTtnFormat() {
        TelemetryUplinkRequest request = TelemetryUplinkRequest.builder()
                .format("TTN")
                .nodeId("NODE-002")
                .fCnt(50L)
                .batteryLevel(85.0)
                .measurements(Map.of("TEMPERATURE", 28.4))
                .build();

        assertThat(ttnDecoder.supports(request)).isTrue();
        DecodedTelemetryPayload decoded = ttnDecoder.decode(request);

        assertThat(decoded).isNotNull();
        assertThat(decoded.getFCnt()).isEqualTo(50L);
        assertThat(decoded.getMeasurements()).containsEntry(SensorType.TEMPERATURE, 28.4);
    }

    @Test
    void jsonDecoderShouldFallbackDecodeGenericJson() {
        TelemetryUplinkRequest request = TelemetryUplinkRequest.builder()
                .nodeId("NODE-003")
                .measurements(Map.of("HUMIDITY", 65.0))
                .build();

        assertThat(jsonDecoder.supports(request)).isTrue();
        DecodedTelemetryPayload decoded = jsonDecoder.decode(request);

        assertThat(decoded).isNotNull();
        assertThat(decoded.getNodeId()).isEqualTo("NODE-003");
        assertThat(decoded.getMeasurements()).containsEntry(SensorType.HUMIDITY, 65.0);
    }
}

