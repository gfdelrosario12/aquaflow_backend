package com.aquaflow.backend.api;

import com.aquaflow.backend.domain.TelemetryIngestionService;
import com.aquaflow.backend.dto.request.TelemetryBatchUplinkRequest;
import com.aquaflow.backend.dto.request.TelemetryUplinkRequest;
import com.aquaflow.backend.dto.response.TelemetryBatchUplinkResponse;
import com.aquaflow.backend.dto.response.TelemetryUplinkResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/telemetry")
@RequiredArgsConstructor
public class TelemetryIngestionController {

    private final TelemetryIngestionService telemetryIngestionService;

    @PostMapping("/uplink/{nodeId}")
    public ResponseEntity<TelemetryUplinkResponse> ingestNodeUplink(
            @PathVariable String nodeId,
            @RequestBody TelemetryUplinkRequest request) {
        TelemetryUplinkResponse response = telemetryIngestionService.ingestUplinkForNode(nodeId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/uplink")
    public ResponseEntity<TelemetryUplinkResponse> ingestGenericUplink(
            @RequestBody TelemetryUplinkRequest request) {
        TelemetryUplinkResponse response = telemetryIngestionService.ingestUplink(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch-uplink")
    public ResponseEntity<TelemetryBatchUplinkResponse> ingestBatchUplink(
            @Valid @RequestBody TelemetryBatchUplinkRequest request) {
        TelemetryBatchUplinkResponse response = telemetryIngestionService.ingestBatchUplink(request);
        return ResponseEntity.ok(response);
    }
}

