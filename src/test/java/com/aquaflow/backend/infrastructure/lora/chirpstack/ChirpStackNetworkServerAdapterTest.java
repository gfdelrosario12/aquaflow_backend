package com.aquaflow.backend.infrastructure.lora.chirpstack;

import com.aquaflow.backend.infrastructure.lora.chirpstack.dto.ChirpStackDownlinkRequest;
import com.aquaflow.backend.infrastructure.lora.chirpstack.dto.ChirpStackDownlinkResponse;
import com.aquaflow.backend.infrastructure.lora.exception.LoraIntegrationException;
import com.aquaflow.backend.infrastructure.lora.model.DownlinkConfirmationMode;
import com.aquaflow.backend.infrastructure.lora.model.LoraDownlinkMessage;
import com.aquaflow.backend.infrastructure.lora.model.LoraDownlinkResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChirpStackNetworkServerAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    private ChirpStackNetworkServerAdapter adapter;

    private final String apiUrl = "http://localhost:8080";
    private final String apiToken = "test-token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new ChirpStackNetworkServerAdapter(restTemplate, apiUrl, apiToken);
    }

    @Test
    void shouldSupportChirpStackProvider() {
        assertThat(adapter.supports("CHIRPSTACK")).isTrue();
        assertThat(adapter.supports("chirpstack")).isTrue();
        assertThat(adapter.supports(null)).isTrue();
        assertThat(adapter.supports("TTN")).isFalse();
    }

    @Test
    void shouldSendDownlinkSuccessfullyWithConfirmedModeAndHexPayload() {
        LoraDownlinkMessage message = LoraDownlinkMessage.builder()
                .devEui("0011223344556677")
                .correlationId("corr-123")
                .fPort(15)
                .payloadHex("010203")
                .confirmationMode(DownlinkConfirmationMode.CONFIRMED)
                .build();

        ChirpStackDownlinkResponse mockResponse = ChirpStackDownlinkResponse.builder()
                .id("queue-item-456")
                .fCnt(42L)
                .build();

        when(restTemplate.postForEntity(
                eq("http://localhost:8080/api/devices/0011223344556677/queue"),
                any(HttpEntity.class),
                eq(ChirpStackDownlinkResponse.class)
        )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        LoraDownlinkResult result = adapter.sendDownlink(message);

        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getDevEui()).isEqualTo("0011223344556677");
        assertThat(result.getDownlinkId()).isEqualTo("queue-item-456");
        assertThat(result.getCorrelationId()).isEqualTo("corr-123");
        assertThat(result.getFCnt()).isEqualTo(42L);

        ArgumentCaptor<HttpEntity<ChirpStackDownlinkRequest>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).postForEntity(
                eq("http://localhost:8080/api/devices/0011223344556677/queue"),
                captor.capture(),
                eq(ChirpStackDownlinkResponse.class)
        );

        HttpEntity<ChirpStackDownlinkRequest> sentEntity = captor.getValue();
        HttpHeaders headers = sentEntity.getHeaders();
        assertThat(headers.getFirst("Authorization")).isEqualTo("Bearer test-token");
        assertThat(headers.getFirst("X-Correlation-ID")).isEqualTo("corr-123");

        ChirpStackDownlinkRequest reqBody = sentEntity.getBody();
        assertThat(reqBody).isNotNull();
        assertThat(reqBody.getQueueItem().getDevEui()).isEqualTo("0011223344556677");
        assertThat(reqBody.getQueueItem().isConfirmed()).isTrue();
        assertThat(reqBody.getQueueItem().getFPort()).isEqualTo(15);
        assertThat(reqBody.getQueueItem().getData()).isEqualTo("AQID"); // Base64 of 0x010203
    }

    @Test
    void shouldSendDownlinkWithBase64PayloadAndUnconfirmedMode() {
        LoraDownlinkMessage message = LoraDownlinkMessage.builder()
                .devEui("8899AABBCCDDEEFF")
                .payloadBase64("SGVsbG8=")
                .confirmationMode(DownlinkConfirmationMode.UNCONFIRMED)
                .build();

        ChirpStackDownlinkResponse mockResponse = ChirpStackDownlinkResponse.builder()
                .id("queue-789")
                .build();

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(ChirpStackDownlinkResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        LoraDownlinkResult result = adapter.sendDownlink(message);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getDownlinkId()).isEqualTo("queue-789");
        assertThat(result.getCorrelationId()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenDevEuiIsMissing() {
        LoraDownlinkMessage message = LoraDownlinkMessage.builder().build();

        assertThatThrownBy(() -> adapter.sendDownlink(message))
                .isInstanceOf(LoraIntegrationException.class)
                .hasMessageContaining("DevEUI is required");
    }

    @Test
    void shouldHandleHttpErrorFromChirpStackApi() {
        LoraDownlinkMessage message = LoraDownlinkMessage.builder()
                .devEui("0011223344556677")
                .build();

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(ChirpStackDownlinkResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Device Not Found"));

        assertThatThrownBy(() -> adapter.sendDownlink(message))
                .isInstanceOf(LoraIntegrationException.class)
                .hasMessageContaining("CHIRPSTACK_DOWNLINK_FAILED");
    }

    @Test
    void shouldHandleConnectionErrorFromChirpStackApi() {
        LoraDownlinkMessage message = LoraDownlinkMessage.builder()
                .devEui("0011223344556677")
                .build();

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(ChirpStackDownlinkResponse.class)))
                .thenThrow(new RuntimeException("Connection refused"));

        assertThatThrownBy(() -> adapter.sendDownlink(message))
                .isInstanceOf(LoraIntegrationException.class)
                .hasMessageContaining("CHIRPSTACK_CONNECTION_ERROR");
    }
}

