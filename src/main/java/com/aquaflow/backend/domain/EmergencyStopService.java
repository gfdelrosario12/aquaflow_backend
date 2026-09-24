package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.EmergencyStopRequest;
import com.aquaflow.backend.dto.response.CommandStatusResponse;

public interface EmergencyStopService {
    CommandStatusResponse executeEmergencyStop(EmergencyStopRequest request);
}

