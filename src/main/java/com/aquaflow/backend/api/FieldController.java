package com.aquaflow.backend.api;

import com.aquaflow.backend.domain.FieldService;
import com.aquaflow.backend.domain.FieldTopologyService;
import com.aquaflow.backend.domain.ZoneAggregationService;
import com.aquaflow.backend.dto.request.FieldRequest;
import com.aquaflow.backend.dto.response.FieldResponse;
import com.aquaflow.backend.dto.response.FieldTopologyResponse;
import com.aquaflow.backend.dto.response.ZoneTelemetryResponse;
import com.aquaflow.backend.dto.response.ZoneTelemetryTrendResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fields")
public class FieldController {

    private final FieldService fieldService;
    private final FieldTopologyService fieldTopologyService;
    private final ZoneAggregationService zoneAggregationService;

    public FieldController(FieldService fieldService, FieldTopologyService fieldTopologyService) {
    public FieldController(FieldService fieldService,
                           FieldTopologyService fieldTopologyService,
                           ZoneAggregationService zoneAggregationService) {
        this.fieldService = fieldService;
        this.fieldTopologyService = fieldTopologyService;
        this.zoneAggregationService = zoneAggregationService;
    }

    @PostMapping
    public ResponseEntity<FieldResponse> createField(@Valid @RequestBody FieldRequest request) {
        FieldResponse created = fieldService.createField(request);
        return ResponseEntity.created(URI.create("/api/v1/fields/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldResponse> getFieldById(@PathVariable Long id) {
        return ResponseEntity.ok(fieldService.getFieldById(id));
    }

    @GetMapping
    public ResponseEntity<List<FieldResponse>> getAllFields() {
        return ResponseEntity.ok(fieldService.getAllFields());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<FieldResponse>> getAllFieldsPaged(Pageable pageable) {
        return ResponseEntity.ok(fieldService.getAllFields(pageable));
    }

    @GetMapping("/{id}/topology")
    public ResponseEntity<FieldTopologyResponse> getFieldTopology(@PathVariable Long id) {
        return ResponseEntity.ok(fieldTopologyService.getFieldTopology(id));
    }

    @GetMapping("/{fieldId}/zones/{zoneId}/telemetry/latest")
    public ResponseEntity<ZoneTelemetryResponse> getLatestZoneTelemetry(
            @PathVariable Long fieldId,
            @PathVariable Long zoneId) {
        return ResponseEntity.ok(zoneAggregationService.getLatestZoneTelemetry(zoneId));
    }

    @GetMapping("/{fieldId}/zones/{zoneId}/trend")
    public ResponseEntity<ZoneTelemetryTrendResponse> getZoneTelemetryTrend(
            @PathVariable Long fieldId,
            @PathVariable Long zoneId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(zoneAggregationService.getZoneTelemetryTrend(fieldId, zoneId, startTime, endTime));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FieldResponse> updateField(@PathVariable Long id, @Valid @RequestBody FieldRequest request) {
        return ResponseEntity.ok(fieldService.updateField(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteField(@PathVariable Long id) {
        fieldService.deleteField(id);
    }
}

