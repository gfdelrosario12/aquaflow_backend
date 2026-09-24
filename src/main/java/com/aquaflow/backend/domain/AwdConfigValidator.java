package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.AutoIrrigationConfigRequest;
import com.aquaflow.backend.dto.request.AwdThresholdConfigRequest;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class AwdConfigValidator {

    public void validate(AutoIrrigationConfigRequest request) {
        if (request == null) {
            throw new ValidationException("AWD configuration request must not be null", "INVALID_AWD_CONFIG");
        }

        if (request.getMaxDurationMinutes() != null) {
            if (request.getMaxDurationMinutes() <= 0 || request.getMaxDurationMinutes() > 1440) {
                throw new ValidationException("maxDurationMinutes must be between 1 and 1440 minutes", "INVALID_MAX_DURATION");
            }
        }

        if (request.getMinCooldownMinutes() != null) {
            if (request.getMinCooldownMinutes() < 0 || request.getMinCooldownMinutes() > 1440) {
                throw new ValidationException("minCooldownMinutes must be between 0 and 1440 minutes", "INVALID_MIN_COOLDOWN");
            }
        }

        if (request.getAllowedStartHour() != null) {
            if (request.getAllowedStartHour() < 0 || request.getAllowedStartHour() >= 24) {
                throw new ValidationException("allowedStartHour must be between 0 and 23", "INVALID_OPERATING_HOURS");
            }
        }

        if (request.getAllowedEndHour() != null) {
            if (request.getAllowedEndHour() < 0 || request.getAllowedEndHour() >= 24) {
                throw new ValidationException("allowedEndHour must be between 0 and 23", "INVALID_OPERATING_HOURS");
            }
        }

        if (request.getAllowedStartHour() != null && request.getAllowedEndHour() != null) {
            if (request.getAllowedEndHour() < request.getAllowedStartHour()) {
                throw new ValidationException("allowedEndHour cannot be earlier than allowedStartHour", "INVALID_OPERATING_HOURS");
            }
        }

        if (request.getMinConfidenceThreshold() != null) {
            if (request.getMinConfidenceThreshold() < 0.0 || request.getMinConfidenceThreshold() > 1.0) {
                throw new ValidationException("minConfidenceThreshold must be between 0.0 and 1.0", "INVALID_CONFIDENCE_THRESHOLD");
            }
        }

        if (request.getRainDelayHours() != null && request.getRainDelayHours() < 0) {
            throw new ValidationException("rainDelayHours cannot be negative", "INVALID_RAIN_DELAY");
        }

        if (request.getTargetFloodDepthCm() != null && request.getTargetFloodDepthCm() < 0.0) {
            throw new ValidationException("targetFloodDepthCm cannot be negative", "INVALID_FLOOD_DEPTH");
        }

        if (request.getThresholds() != null) {
            for (AwdThresholdConfigRequest threshold : request.getThresholds()) {
                validateThreshold(threshold);
            }
        }
    }

    private void validateThreshold(AwdThresholdConfigRequest threshold) {
        if (threshold == null) return;

        if (threshold.getGrowthStage() == null) {
            throw new ValidationException("Growth stage must be specified for threshold", "INVALID_THRESHOLD");
        }

        Double trigger = threshold.getTriggerMoisturePercentage();
        Double target = threshold.getTargetMoisturePercentage();

        if (trigger != null) {
            if (trigger < 0.0 || trigger > 100.0) {
                throw new ValidationException("triggerMoisturePercentage must be between 0.0 and 100.0", "INVALID_MOISTURE_LIMIT");
            }
        }

        if (target != null) {
            if (target < 0.0 || target > 100.0) {
                throw new ValidationException("targetMoisturePercentage must be between 0.0 and 100.0", "INVALID_MOISTURE_LIMIT");
            }
        }

        if (trigger != null && target != null) {
            if (trigger > target) {
                throw new ValidationException("triggerMoisturePercentage cannot be greater than targetMoisturePercentage", "INVERTED_MOISTURE_THRESHOLDS");
            }
        }

        if (threshold.getTargetFloodDepthCm() != null && threshold.getTargetFloodDepthCm() < 0.0) {
            throw new ValidationException("threshold targetFloodDepthCm cannot be negative", "INVALID_FLOOD_DEPTH");
        }
    }
}

