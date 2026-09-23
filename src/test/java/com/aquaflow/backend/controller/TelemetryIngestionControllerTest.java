package com.aquaflow.backend.controller;

import com.aquaflow.backend.api.TelemetryIngestionController;
import com.aquaflow.backend.domain.TelemetryIngestionService;
import com.aquaflow.backend.dto.request.TelemetryBatchUplinkRequest;
import com.aquaflow.backend.dto.request.TelemetryUplinkRequest;
import com.aquaflow.backend.dto.response.TelemetryBatchUplinkResponse;
import com.aquaflow.backend.dto.response.TelemetryUplinkResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class TelemetryIngestionControllerTest {

    @Mock
    private TelemetryIngestionService telemetryIngestionService;

    private TelemetryIngestionController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new TelemetryIngestionController(telemetryIngestionService);
    }

    @Test
    void shouldIngestNodeUplink() {
        TelemetryUplinkRequest request = TelemetryUplinkRequest.builder().fCnt(10L).build();
        TelemetryUplinkResponse response = TelemetryUplinkResponse.builder()
                .success(true)
                .nodeId("NODE-001")
                .message("Telemetry uplink ingested successfully")
                .readingsIngested(1)
                .build();

        when(telemetryIngestionService.ingestUplinkForNode(eq("NODE-001"), any(TelemetryUplinkRequest.class))).thenReturn(response);

        ResponseEntity<TelemetryUplinkResponse> result = controller.ingestNodeUplink("NODE-001", request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getNodeId()).isEqualTo("NODE-001");
        assertThat(result.getBody().isSuccess()).isTrue();
    }

    @Test
    void shouldIngestGenericUplink() {
        TelemetryUplinkRequest request = TelemetryUplinkRequest.builder().nodeId("NODE-002").build();
        TelemetryUplinkResponse response = TelemetryUplinkResponse.builder().success(true).nodeId("NODE-002").build();

        when(telemetryIngestionService.ingestUplink(any(TelemetryUplinkRequest.class))).thenReturn(response);

        ResponseEntity<TelemetryUplinkResponse> result = controller.ingestGenericUplink(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getNodeId()).isEqualTo("NODE-002");
    }

    @Test
    void shouldIngestBatchUplink() {
        TelemetryUplinkRequest req = TelemetryUplinkRequest.builder().nodeId("NODE-003").build();
        TelemetryBatchUplinkRequest batchRequest = TelemetryBatchUplinkRequest.builder().uplinks(List.of(req)).build();

        TelemetryBatchUplinkResponse response = TelemetryBatchUplinkResponse.builder()
                .totalProcessed(1)
                .successful(1)
                .build();

        when(telemetryIngestionService.ingestBatchUplink(any(TelemetryBatchUplinkRequest.class))).thenReturn(response);

        ResponseEntity<TelemetryBatchUplinkResponse> result = controller.ingestBatchUplink(batchRequest);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getTotalProcessed()).isEqualTo(1);
    }
}

