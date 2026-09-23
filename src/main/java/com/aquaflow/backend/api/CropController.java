package com.aquaflow.backend.api;

import com.aquaflow.backend.dto.request.CropRequest;
import com.aquaflow.backend.dto.response.CropResponse;
import com.aquaflow.backend.domain.CropService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/crops")
public class CropController {

    private static final Logger log = LoggerFactory.getLogger(CropController.class);

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @PostMapping
    public ResponseEntity<CropResponse> createCrop(@RequestBody CropRequest request) {
        log.info("POST /api/v1/crops");
        CropResponse response = cropService.createCrop(request);
        return ResponseEntity.created(URI.create("/api/v1/crops/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CropResponse> getCropById(@PathVariable Long id) {
        log.info("GET /api/v1/crops/{}", id);
        return ResponseEntity.ok(cropService.getCropById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CropResponse>> getAllCrops(Pageable pageable) {
        log.info("GET /api/v1/crops");
        return ResponseEntity.ok(cropService.getAllCrops(pageable));
    }

    @GetMapping
    public ResponseEntity<List<CropResponse>> getAllCropsList() {
        log.info("GET /api/v1/crops (all)");
        return ResponseEntity.ok(cropService.getAllCrops());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CropResponse> updateCrop(@PathVariable Long id, @RequestBody CropRequest request) {
        log.info("PUT /api/v1/crops/{}", id);
        return ResponseEntity.ok(cropService.updateCrop(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCrop(@PathVariable Long id) {
        log.info("DELETE /api/v1/crops/{}", id);
        cropService.deleteCrop(id);
        return ResponseEntity.noContent().build();
    }
}