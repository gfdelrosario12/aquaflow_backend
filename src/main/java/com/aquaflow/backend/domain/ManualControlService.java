package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.ManualStartRequest;
import com.aquaflow.backend.dto.request.ManualStopRequest;
import com.aquaflow.backend.dto.response.CommandStatusResponse;
import com.aquaflow.backend.entity.CommandLifecycleState;

public interface ManualControlService {
    CommandStatusResponse startManualIrrigation(ManualStartRequest request);
    CommandStatusResponse stopManualIrrigation(ManualStopRequest request);
    CommandStatusResponse getCommandStatus(String correlationId);
    void updateCommandStatus(String correlationId, CommandLifecycleState newState, String details);
}

