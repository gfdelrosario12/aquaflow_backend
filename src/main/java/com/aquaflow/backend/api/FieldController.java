package com.aquaflow.backend.api;

import com.aquaflow.backend.domain.FieldService;
import com.aquaflow.backend.domain.FieldTopologyService;
import com.aquaflow.backend.dto.request.FieldRequest;
import com.aquaflow.backend.dto.response.FieldResponse;
import com.aquaflow.backend.dto.response.FieldTopologyResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fields")
public class FieldController {

    private final FieldService fieldService;
    private final FieldTopologyService fieldTopologyService;

    public FieldController(FieldService fieldService, FieldTopologyService fieldTopologyService) {
        this.fieldService = fieldService;
        this.fieldTopologyService = fieldTopologyService;
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

