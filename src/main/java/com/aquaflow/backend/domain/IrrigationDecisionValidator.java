package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.IrrigationDecisionRequest;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class IrrigationDecisionValidator {

    public void validate(IrrigationDecisionRequest request) {
        if (request == null) {
            throw new ValidationException("Irrigation decision request must not be null", "INVALID_DECISION");
        }

        if (request.getEdgeNodeId() == null && (request.getNodeId() == null || request.getNodeId().isBlank())) {
            throw new ValidationException("Either edgeNodeId or nodeId must be specified", "INVALID_NODE_IDENTITY");
        }

        if (request.getDecisionType() == null) {
            throw new ValidationException("decisionType must be specified", "INVALID_DECISION_TYPE");
        }

        if (request.getTriggerReason() == null) {
            throw new ValidationException("triggerReason must be specified", "INVALID_TRIGGER_REASON");
        }

        if (request.getRequestedDurationMinutes() != null && request.getRequestedDurationMinutes() < 0) {
            throw new ValidationException("requestedDurationMinutes cannot be negative", "INVALID_DURATION");
        }

        if (request.getRequestedVolumeLiters() != null && request.getRequestedVolumeLiters() < 0.0) {
            throw new ValidationException("requestedVolumeLiters cannot be negative", "INVALID_VOLUME");
        }

        if (request.getActualDurationMinutes() != null && request.getActualDurationMinutes() < 0) {
            throw new ValidationException("actualDurationMinutes cannot be negative", "INVALID_DURATION");
        }

        if (request.getConfidence() != null) {
            if (request.getConfidence() < 0.0 || request.getConfidence() > 1.0) {
                throw new ValidationException("confidence score must be between 0.0 and 1.0", "INVALID_CONFIDENCE");
            }
        }
    }
}

