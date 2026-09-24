package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.IrrigationDecisionRequest;
import com.aquaflow.backend.dto.response.FieldIrrigationStatusResponse;
import com.aquaflow.backend.dto.response.IrrigationDecisionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IrrigationDecisionService {
    IrrigationDecisionResponse reportDecision(IrrigationDecisionRequest request);
    Page<IrrigationDecisionResponse> getDecisions(Long edgeNodeId, String executionStatus, Pageable pageable);
    IrrigationDecisionResponse getDecisionById(Long id);
    FieldIrrigationStatusResponse getFieldIrrigationStatus(Long fieldId);
}

