package com.aquaflow.backend.api;

import com.aquaflow.backend.dto.request.ZoneRequest;
import com.aquaflow.backend.dto.response.ZoneResponse;
import com.aquaflow.backend.domain.ZoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/zones")
public class ZoneController {

    private static final Logger log = LoggerFactory.getLogger(ZoneController.class);

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping
    public ResponseEntity<ZoneResponse> createZone(@RequestBody ZoneRequest request) {
        log.info("POST /api/v1/zones");
        ZoneResponse response = zoneService.createZone(request);
        return ResponseEntity.created(URI.create("/api/v1/zones/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneResponse> getZoneById(@PathVariable Long id) {
        log.info("GET /api/v1/zones/{}", id);
        return ResponseEntity.ok(zoneService.getZoneById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ZoneResponse>> getAllZones(Pageable pageable) {
        log.info("GET /api/v1/zones");
        return ResponseEntity.ok(zoneService.getAllZones(pageable));
    }

    @GetMapping
    public ResponseEntity<List<ZoneResponse>> getAllZonesList() {
        log.info("GET /api/v1/zones (all)");
        return ResponseEntity.ok(zoneService.getAllZones());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZoneResponse> updateZone(@PathVariable Long id, @RequestBody ZoneRequest request) {
        log.info("PUT /api/v1/zones/{}", id);
        return ResponseEntity.ok(zoneService.updateZone(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        log.info("DELETE /api/v1/zones/{}", id);
        zoneService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/crop-type/{cropType}")
    public ResponseEntity<List<ZoneResponse>> getZonesByCropType(@PathVariable String cropType) {
        log.info("GET /api/v1/zones/crop-type/{}", cropType);
        return ResponseEntity.ok(zoneService.getZonesByCropType(cropType));
    }
}