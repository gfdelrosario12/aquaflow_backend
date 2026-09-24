package com.aquaflow.backend.domain;

import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.entity.NodeHealthMetrics;
import com.aquaflow.backend.infrastructure.event.SystemEventPublisher;
import com.aquaflow.backend.persistence.EdgeNodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TelemetryFreshnessChecker {

    private static final Logger log = LoggerFactory.getLogger(TelemetryFreshnessChecker.class);

    private final EdgeNodeRepository edgeNodeRepository;
    private final SystemEventPublisher systemEventPublisher;

    @Autowired
    public TelemetryFreshnessChecker(EdgeNodeRepository edgeNodeRepository,
                                    @Autowired(required = false) SystemEventPublisher systemEventPublisher) {
        this.edgeNodeRepository = edgeNodeRepository;
        this.systemEventPublisher = systemEventPublisher;
    }

    @Scheduled(fixedDelay = 60000)
    public void checkTelemetryFreshness() {
        log.info("Running periodic telemetry freshness check");
        List<EdgeNode> nodes = edgeNodeRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (EdgeNode node : nodes) {
            try {
                NodeHealthMetrics metrics = node.getHealthMetrics();
                if (metrics == null) {
                    metrics = new NodeHealthMetrics();
                    node.setHealthMetrics(metrics);
                }

                LocalDateTime lastTelemetry = metrics.getLastTelemetryAt() != null
                        ? metrics.getLastTelemetryAt()
                        : metrics.getLastHeartbeat();

                if (lastTelemetry == null) {
                    lastTelemetry = node.getUpdatedAt() != null ? node.getUpdatedAt() : node.getCreatedAt();
                }

                long minutesSinceLastTelemetry = lastTelemetry != null
                        ? Duration.between(lastTelemetry, now).toMinutes()
                        : 999;

                boolean wasStale = Boolean.TRUE.equals(metrics.getIsTelemetryStale());
                boolean isStale = minutesSinceLastTelemetry >= 30;

                if (wasStale != isStale) {
                    log.info("Telemetry freshness status changed for node {}: stale={}", node.getNodeId(), isStale);
                    metrics.setIsTelemetryStale(isStale);
                    edgeNodeRepository.save(node);

                    if (isStale && systemEventPublisher != null) {
                        Long fieldId = null;
                        if (node.getMonitoringZone() != null && node.getMonitoringZone().getField() != null) {
                            fieldId = node.getMonitoringZone().getField().getId();
                        }

                        systemEventPublisher.publishAlarm(
                                node.getNodeId(),
                                fieldId,
                                "STALE_TELEMETRY",
                                "WARNING",
                                String.format("Telemetry data for node %s is stale (%d mins old)", node.getNodeId(), minutesSinceLastTelemetry)
                        );
                    }
                }
            } catch (Exception e) {
                log.error("Error checking telemetry freshness for node {}: {}", node.getNodeId(), e.getMessage());
            }
        }
    }
}

