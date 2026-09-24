package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.response.ConfigSyncStatusResponse;

public interface EdgeNodeSyncService {
    ConfigSyncStatusResponse getSyncStatus(Long edgeNodeId);
    ConfigSyncStatusResponse triggerSyncForNode(Long edgeNodeId, String reason);
    void syncConfigForField(Long fieldId);
    void processAcknowledgement(String correlationId);
    void checkTaskTimeouts();
}

