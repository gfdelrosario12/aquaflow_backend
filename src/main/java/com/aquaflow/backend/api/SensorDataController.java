package com.aquaflow.backend.api;

import com.aquaflow.backend.dto.request.SensorDataRequest;
import com.aquaflow.backend.dto.response.SensorDataResponse;
import com.aquaflow.backend.domain.SensorDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sensors/data")
public class SensorDataController {

    private static final Logger log = LoggerFactory.getLogger(SensorDataController.class);

    private final SensorDataService sensorDataService;

    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<SensorDataResponse> ingestSensorData(@RequestBody SensorDataRequest request) {
        log.info("POST /api/v1/sensors/data/ingest");
        SensorDataResponse response = sensorDataService.ingestSensorData(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDataResponse> getReadingById(@PathVariable Long id) {
        log.info("GET /api/v1/sensors/data/{}", id);
        return ResponseEntity.ok(sensorDataService.getReadingById(id));
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<Page<SensorDataResponse>> getReadingsByDeviceId(
            @PathVariable String deviceId, Pageable pageable) {
        log.info("GET /api/v1/sensors/data/device/{}", deviceId);
        return ResponseEntity.ok(sensorDataService.getReadingsByDeviceId(deviceId, pageable));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<Page<SensorDataResponse>> getReadingsByZoneId(
            @PathVariable Long zoneId, Pageable pageable) {
        log.info("GET /api/v1/sensors/data/zone/{}", zoneId);
        return ResponseEntity.ok(sensorDataService.getReadingsByZoneId(zoneId, pageable));
    }

    @GetMapping("/device/{deviceId}/range")
    public ResponseEntity<Page<SensorDataResponse>> getReadingsByDeviceIdAndTimeRange(
            @PathVariable String deviceId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to,
            Pageable pageable) {
        log.info("GET /api/v1/sensors/data/device/{}/range", deviceId);
        return ResponseEntity.ok(sensorDataService.getReadingsByDeviceIdAndTimeRange(deviceId, from, to, pageable));
    }

    @GetMapping("/device/{deviceId}/all")
    public ResponseEntity<List<SensorDataResponse>> getReadingsByDeviceIdAll(@PathVariable String deviceId) {
        log.info("GET /api/v1/sensors/data/device/{}/all", deviceId);
        return ResponseEntity.ok(sensorDataService.getReadingsByDeviceId(deviceId));
    }
}