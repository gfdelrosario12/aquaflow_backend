package com.aquaflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeHealthSummaryResponse {
    private int totalNodes;
    private int healthyNodesCount;
    private int degradedNodesCount;
    private int criticalNodesCount;
    private int offlineNodesCount;
    private int staleTelemetryNodesCount;
    private List<NodeHealthResponse> nodes;
}

