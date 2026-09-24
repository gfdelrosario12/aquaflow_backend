package com.aquaflow.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeTelemetryWeightRequest {

    private String nodeId;
    private Double spatialWeight;

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public Double getSpatialWeight() { return spatialWeight; }
    public void setSpatialWeight(Double spatialWeight) { this.spatialWeight = spatialWeight; }
}

