package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.AutoIrrigationConfigRequest;
import com.aquaflow.backend.dto.request.AwdThresholdConfigRequest;
import com.aquaflow.backend.entity.CropGrowthStage;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AwdConfigValidatorTest {

    private AwdConfigValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AwdConfigValidator();
    }

    @Test
    void shouldPassValidationForValidConfig() {
        AutoIrrigationConfigRequest request = AutoIrrigationConfigRequest.builder()
                .enabled(true)
                .maxDurationMinutes(120)
                .minCooldownMinutes(180)
                .allowedStartHour(6)
                .allowedEndHour(18)
                .targetFloodDepthCm(5.0)
                .rainDelayHours(24)
                .minConfidenceThreshold(0.85)
                .thresholds(List.of(
                        AwdThresholdConfigRequest.builder()
                                .growthStage(CropGrowthStage.VEGETATIVE)
                                .triggerMoisturePercentage(40.0)
                                .targetMoisturePercentage(80.0)
                                .targetFloodDepthCm(5.0)
                                .build()
                ))
                .build();

        assertThatNoException().isThrownBy(() -> validator.validate(request));
    }

    @Test
    void shouldRejectInvalidMaxDuration() {
        AutoIrrigationConfigRequest request = AutoIrrigationConfigRequest.builder()
                .maxDurationMinutes(2000) // exceeds 1440
                .build();

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("maxDurationMinutes must be between 1 and 1440 minutes");
    }

    @Test
    void shouldRejectInvalidOperatingHours() {
        AutoIrrigationConfigRequest request = AutoIrrigationConfigRequest.builder()
                .allowedStartHour(18)
                .allowedEndHour(6) // end earlier than start
                .build();

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("allowedEndHour cannot be earlier than allowedStartHour");
    }

    @Test
    void shouldRejectInvertedMoistureThresholds() {
        AutoIrrigationConfigRequest request = AutoIrrigationConfigRequest.builder()
                .thresholds(List.of(
                        AwdThresholdConfigRequest.builder()
                                .growthStage(CropGrowthStage.REPRODUCTIVE)
                                .triggerMoisturePercentage(85.0) // trigger > target!
                                .targetMoisturePercentage(40.0)
                                .build()
                ))
                .build();

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("triggerMoisturePercentage cannot be greater than targetMoisturePercentage");
    }

    @Test
    void shouldRejectInvalidConfidenceThreshold() {
        AutoIrrigationConfigRequest request = AutoIrrigationConfigRequest.builder()
                .minConfidenceThreshold(1.5) // > 1.0
                .build();

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("minConfidenceThreshold must be between 0.0 and 1.0");
    }
}

