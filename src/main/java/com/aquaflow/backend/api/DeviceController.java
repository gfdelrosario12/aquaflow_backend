package com.aquaflow.backend.api;

import com.aquaflow.backend.dto.request.DeviceRequest;
import com.aquaflow.backend.dto.response.DeviceResponse;
import com.aquaflow.backend.domain.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<DeviceResponse> registerDevice(@RequestBody DeviceRequest request) {
        log.info("POST /api/v1/devices");
        DeviceResponse response = deviceService.registerDevice(request);
        return ResponseEntity.created(URI.create("/api/v1/devices/" + response.getDeviceId())).body(response);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable String deviceId) {
        log.info("GET /api/v1/devices/{}", deviceId);
        return ResponseEntity.ok(deviceService.getDeviceById(deviceId));
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getAllDevices() {
        log.info("GET /api/v1/devices");
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<DeviceResponse> updateDevice(@PathVariable String deviceId, @RequestBody DeviceRequest request) {
        log.info("PUT /api/v1/devices/{}", deviceId);
        return ResponseEntity.ok(deviceService.updateDevice(deviceId, request));
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String deviceId) {
        log.info("DELETE /api/v1/devices/{}", deviceId);
        deviceService.deleteDevice(deviceId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{deviceId}/heartbeat")
    public ResponseEntity<DeviceResponse> updateHeartbeat(@PathVariable String deviceId) {
        log.info("POST /api/v1/devices/{}/heartbeat", deviceId);
        return ResponseEntity.ok(deviceService.updateHeartbeat(deviceId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeviceResponse>> getDevicesByStatus(@PathVariable String status) {
        log.info("GET /api/v1/devices/status/{}", status);
        return ResponseEntity.ok(deviceService.getDevicesByStatus(status));
    }
}