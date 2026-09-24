package com.aquaflow.backend.domain;

import com.aquaflow.backend.entity.DownlinkQueueItem;
import com.aquaflow.backend.entity.EdgeNode;

import java.util.List;

public interface DownlinkQueueService {
    DownlinkQueueItem queueDownlink(EdgeNode edgeNode, String payloadJson, String correlationId);
    boolean transmitDownlink(DownlinkQueueItem item);
    void handleAcknowledgement(String correlationId);
    void processTimeoutsAndRetries();
    List<DownlinkQueueItem> getQueueForNode(Long edgeNodeId);
}

