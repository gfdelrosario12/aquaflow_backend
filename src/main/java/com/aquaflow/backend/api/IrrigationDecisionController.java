package com.aquaflow.backend.api;

import com.aquaflow.backend.domain.IrrigationDecisionService;
import com.aquaflow.backend.dto.request.IrrigationDecisionRequest;
import com.aquaflow.backend.dto.response.FieldIrrigationStatusResponse;
import com.aquaflow.backend.dto.response.IrrigationDecisionResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/irrigation")
public class IrrigationDecisionController {

    private final IrrigationDecisionService decisionService;

    public IrrigationDecisionController(IrrigationDecisionService decisionService) {
        this.decisionService = decisionService;
    }

    @PostMapping("/decisions")
    public ResponseEntity<IrrigationDecisionResponse> reportDecision(@Valid @RequestBody IrrigationDecisionRequest request) {
        IrrigationDecisionResponse response = decisionService.reportDecision(request);
        return ResponseEntity.created(URI.create("/api/v1/irrigation/decisions/" + response.getId())).body(response);
    }

    @GetMapping("/decisions")
    public ResponseEntity<Page<IrrigationDecisionResponse>> getDecisions(
            @RequestParam(required = false) Long edgeNodeId,
            @RequestParam(required = false) String executionStatus,
            Pageable pageable) {
        return ResponseEntity.ok(decisionService.getDecisions(edgeNodeId, executionStatus, pageable));
    }

    @GetMapping("/decisions/{id}")
    public ResponseEntity<IrrigationDecisionResponse> getDecisionById(@PathVariable Long id) {
        return ResponseEntity.ok(decisionService.getDecisionById(id));
    }

    @GetMapping("/field/{fieldId}/status")
    public ResponseEntity<FieldIrrigationStatusResponse> getFieldIrrigationStatus(@PathVariable Long fieldId) {
        return ResponseEntity.ok(decisionService.getFieldIrrigationStatus(fieldId));
    }
}

