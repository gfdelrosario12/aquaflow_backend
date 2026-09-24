package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.AutoIrrigationConfigRequest;
import com.aquaflow.backend.dto.response.AutoIrrigationConfigResponse;

public interface AwdConfigService {
    AutoIrrigationConfigResponse getAwdConfig(Long fieldId);
    AutoIrrigationConfigResponse updateAwdConfig(Long fieldId, AutoIrrigationConfigRequest request);
}

