package com.aquaflow.backend.api;

import com.aquaflow.backend.domain.AwdConfigService;
import com.aquaflow.backend.dto.request.AutoIrrigationConfigRequest;
import com.aquaflow.backend.dto.response.AutoIrrigationConfigResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fields/{fieldId}/awd-config")
public class AwdConfigController {

    private final AwdConfigService awdConfigService;

    public AwdConfigController(AwdConfigService awdConfigService) {
        this.awdConfigService = awdConfigService;
    }

    @GetMapping
    public ResponseEntity<AutoIrrigationConfigResponse> getAwdConfig(@PathVariable Long fieldId) {
        return ResponseEntity.ok(awdConfigService.getAwdConfig(fieldId));
    }

    @PutMapping
    public ResponseEntity<AutoIrrigationConfigResponse> updateAwdConfig(
            @PathVariable Long fieldId,
            @RequestBody AutoIrrigationConfigRequest request) {
        return ResponseEntity.ok(awdConfigService.updateAwdConfig(fieldId, request));
    }
}

