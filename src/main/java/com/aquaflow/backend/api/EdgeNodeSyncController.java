package com.aquaflow.backend.api;

import com.aquaflow.backend.domain.EdgeNodeSyncService;
import com.aquaflow.backend.dto.request.ConfigSyncTriggerRequest;
import com.aquaflow.backend.dto.response.ConfigSyncStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nodes/{nodeId}/config-sync")
public class EdgeNodeSyncController {

    private final EdgeNodeSyncService edgeNodeSyncService;

    public EdgeNodeSyncController(EdgeNodeSyncService edgeNodeSyncService) {
        this.edgeNodeSyncService = edgeNodeSyncService;
    }

    @GetMapping("/status")
    public ResponseEntity<ConfigSyncStatusResponse> getSyncStatus(@PathVariable Long nodeId) {
        return ResponseEntity.ok(edgeNodeSyncService.getSyncStatus(nodeId));
    }

    @PostMapping("/trigger")
    public ResponseEntity<ConfigSyncStatusResponse> triggerSync(
            @PathVariable Long nodeId,
            @RequestBody(required = false) ConfigSyncTriggerRequest request) {
        String reason = (request != null && request.getReason() != null) ? request.getReason() : "Manual REST API trigger";
        return ResponseEntity.ok(edgeNodeSyncService.triggerSyncForNode(nodeId, reason));
    }
}

