package com.aquaflow.backend.domain;

import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.entity.HealthState;
import com.aquaflow.backend.entity.NodeHealthMetrics;
import com.aquaflow.backend.infrastructure.event.SystemEventPublisher;
import com.aquaflow.backend.persistence.EdgeNodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NodeHealthMonitor {

    private static final Logger log = LoggerFactory.getLogger(NodeHealthMonitor.class);

    private final EdgeNodeRepository edgeNodeRepository;
    private final SystemEventPublisher systemEventPublisher;

    @Autowired
    public NodeHealthMonitor(EdgeNodeRepository edgeNodeRepository,
                             @Autowired(required = false) SystemEventPublisher systemEventPublisher) {
        this.edgeNodeRepository = edgeNodeRepository;
        this.systemEventPublisher = systemEventPublisher;
    }

    @Scheduled(fixedDelay = 60000)
    public void evaluateNodeHealth() {
        log.info("Running periodic edge node health monitoring evaluation");
        List<EdgeNode> nodes = edgeNodeRepository.findAll();

        LocalDateTime now = LocalDateTime.now();

        for (EdgeNode node : nodes) {
            try {
                HealthState oldState = node.getHealthState();
                HealthState newState = determineHealthState(node, now);

                if (oldState != newState) {
                    log.info("Node {} health state transition: {} -> {}", node.getNodeId(), oldState, newState);
                    node.setHealthState(newState);
                    edgeNodeRepository.save(node);

                    Long fieldId = null;
                    if (node.getMonitoringZone() != null && node.getMonitoringZone().getField() != null) {
                        fieldId = node.getMonitoringZone().getField().getId();
                    }

                    if (systemEventPublisher != null) {
                        systemEventPublisher.publishNodeStatusChanged(
                                node.getNodeId(),
                                fieldId,
                                oldState != null ? oldState.name() : "UNKNOWN",
                                newState.name()
                        );

                        if (newState == HealthState.CRITICAL || newState == HealthState.OFFLINE) {
                            systemEventPublisher.publishAlarm(
                                    node.getNodeId(),
                                    fieldId,
                                    "NODE_HEALTH_" + newState.name(),
                                    newState == HealthState.OFFLINE ? "CRITICAL" : "HIGH",
                                    String.format("Node %s health transitioned to %s", node.getNodeId(), newState.name())
                            );
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error evaluating health for node {}: {}", node.getNodeId(), e.getMessage());
            }
        }
    }

    public HealthState determineHealthState(EdgeNode node, LocalDateTime now) {
        if (node == null) return HealthState.UNKNOWN;

        NodeHealthMetrics metrics = node.getHealthMetrics();
        LocalDateTime lastHeartbeat = metrics != null && metrics.getLastHeartbeat() != null
                ? metrics.getLastHeartbeat()
                : node.getUpdatedAt();

        if (lastHeartbeat == null) {
            lastHeartbeat = node.getCreatedAt();
        }

        long minutesSinceHeartbeat = lastHeartbeat != null ? java.time.Duration.between(lastHeartbeat, now).toMinutes() : 999;

        if (minutesSinceHeartbeat >= 60) {
            return HealthState.OFFLINE;
        }

        Double battery = metrics != null ? metrics.getBatteryLevel() : null;
        Integer signalDbm = metrics != null ? metrics.getSignalDbm() : null;
        Integer consecutiveFailures = metrics != null ? metrics.getConsecutiveFailures() : null;

        if ((battery != null && battery < 10.0) || (consecutiveFailures != null && consecutiveFailures >= 3)) {
            return HealthState.CRITICAL;
        }

        if ((battery != null && battery < 20.0)
                || (signalDbm != null && signalDbm < -115)
                || minutesSinceHeartbeat >= 30) {
            return HealthState.DEGRADED;
        }

        return HealthState.HEALTHY;
    }
}

