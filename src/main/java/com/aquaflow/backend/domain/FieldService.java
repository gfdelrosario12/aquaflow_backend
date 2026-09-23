package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.FieldRequest;
import com.aquaflow.backend.dto.response.FieldResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FieldService {
    FieldResponse createField(FieldRequest request);
    FieldResponse getFieldById(Long id);
    Page<FieldResponse> getAllFields(Pageable pageable);
    List<FieldResponse> getAllFields();
    FieldResponse updateField(Long id, FieldRequest request);
    void deleteField(Long id);
}

