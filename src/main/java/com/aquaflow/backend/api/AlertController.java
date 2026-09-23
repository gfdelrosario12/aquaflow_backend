package com.aquaflow.backend.api;

import com.aquaflow.backend.dto.response.AlertResponse;
import com.aquaflow.backend.domain.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private static final Logger log = LoggerFactory.getLogger(AlertController.class);

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<AlertResponse> createAlert(
            @RequestParam String deviceId,
            @RequestParam Long zoneId,
            @RequestParam String alertType,
            @RequestParam String alertLevel,
            @RequestParam String message) {
        log.info("POST /api/v1/alerts");
        AlertResponse response = alertService.createAlert(deviceId, zoneId, alertType, alertLevel, message);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{alertId}/acknowledge")
    public ResponseEntity<AlertResponse> acknowledgeAlert(@PathVariable Long alertId) {
        log.info("PATCH /api/v1/alerts/{}/acknowledge", alertId);
        return ResponseEntity.ok(alertService.acknowledgeAlert(alertId));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<Page<AlertResponse>> getAlertsByZoneId(@PathVariable Long zoneId, Pageable pageable) {
        log.info("GET /api/v1/alerts/zone/{}", zoneId);
        return ResponseEntity.ok(alertService.getAlertsByZoneId(zoneId, pageable));
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<Page<AlertResponse>> getAlertsByDeviceId(@PathVariable String deviceId, Pageable pageable) {
        log.info("GET /api/v1/alerts/device/{}", deviceId);
        return ResponseEntity.ok(alertService.getAlertsByDeviceId(deviceId, pageable));
    }

    @GetMapping("/unacknowledged")
    public ResponseEntity<List<AlertResponse>> getUnacknowledgedAlerts() {
        log.info("GET /api/v1/alerts/unacknowledged");
        return ResponseEntity.ok(alertService.getUnacknowledgedAlerts());
    }

    @GetMapping
    public ResponseEntity<List<AlertResponse>> getAllAlerts() {
        log.info("GET /api/v1/alerts");
        return ResponseEntity.ok(alertService.getAllAlerts());
    }
}