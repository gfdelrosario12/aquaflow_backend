package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.IrrigationDecisionValidator;
import com.aquaflow.backend.dto.request.IrrigationDecisionRequest;
import com.aquaflow.backend.entity.DecisionType;
import com.aquaflow.backend.entity.TriggerReason;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IrrigationDecisionValidatorTest {

    private IrrigationDecisionValidator validator;

    @BeforeEach
    void setUp() {
        validator = new IrrigationDecisionValidator();
    }

    @Test
    void shouldValidateValidDecisionRequest() {
        IrrigationDecisionRequest request = IrrigationDecisionRequest.builder()
                .edgeNodeId(1L)
                .decisionType(DecisionType.SOIL_MOISTURE_TRIGGER)
                .triggerReason(TriggerReason.SOIL_MOISTURE_BELOW_MIN)
                .requestedDurationMinutes(60)
                .requestedVolumeLiters(500.0)
                .confidence(0.95)
                .build();

        assertThatNoException().isThrownBy(() -> validator.validate(request));
    }

    @Test
    void shouldRejectMissingNodeIdentity() {
        IrrigationDecisionRequest request = IrrigationDecisionRequest.builder()
                .decisionType(DecisionType.SOIL_MOISTURE_TRIGGER)
                .triggerReason(TriggerReason.SOIL_MOISTURE_BELOW_MIN)
                .build();

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Either edgeNodeId or nodeId must be specified");
    }

    @Test
    void shouldRejectNegativeDuration() {
        IrrigationDecisionRequest request = IrrigationDecisionRequest.builder()
                .edgeNodeId(1L)
                .decisionType(DecisionType.SOIL_MOISTURE_TRIGGER)
                .triggerReason(TriggerReason.SOIL_MOISTURE_BELOW_MIN)
                .requestedDurationMinutes(-10)
                .build();

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("requestedDurationMinutes cannot be negative");
    }

    @Test
    void shouldRejectInvalidConfidenceScore() {
        IrrigationDecisionRequest request = IrrigationDecisionRequest.builder()
                .edgeNodeId(1L)
                .decisionType(DecisionType.SOIL_MOISTURE_TRIGGER)
                .triggerReason(TriggerReason.SOIL_MOISTURE_BELOW_MIN)
                .confidence(1.5) // > 1.0
                .build();

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("confidence score must be between 0.0 and 1.0");
    }
}

