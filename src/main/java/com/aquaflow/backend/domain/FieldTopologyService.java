package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.response.FieldTopologyResponse;

public interface FieldTopologyService {
    FieldTopologyResponse getFieldTopology(Long fieldId);
    void validateNodeAssignmentToZone(Long edgeNodeId, Long monitoringZoneId);
    void validatePointAssignmentToNode(Long monitoringPointId, Long edgeNodeId);
}

