package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.response.AutoIrrigationConfigResponse;
import com.aquaflow.backend.dto.response.ConfigSyncStatusResponse;
import com.aquaflow.backend.entity.AutoIrrigationConfig;
import com.aquaflow.backend.entity.ConfigSyncStatus;
import com.aquaflow.backend.entity.ConfigSyncTask;
import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.entity.MonitoringZone;
import com.aquaflow.backend.infrastructure.event.SystemEvent;
import com.aquaflow.backend.infrastructure.event.SystemEventPublisher;
import com.aquaflow.backend.infrastructure.event.SystemEventType;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.infrastructure.util.DtoMapper;
import com.aquaflow.backend.persistence.AutoIrrigationConfigRepository;
import com.aquaflow.backend.persistence.ConfigSyncTaskRepository;
import com.aquaflow.backend.persistence.EdgeNodeRepository;
import com.aquaflow.backend.persistence.MonitoringZoneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EdgeNodeSyncServiceImpl implements EdgeNodeSyncService {

    private static final Logger log = LoggerFactory.getLogger(EdgeNodeSyncServiceImpl.class);

    private final ConfigSyncTaskRepository syncTaskRepository;
    private final EdgeNodeRepository edgeNodeRepository;
    private final AutoIrrigationConfigRepository autoIrrigationConfigRepository;
    private final MonitoringZoneRepository monitoringZoneRepository;
    private final DownlinkQueueService downlinkQueueService;
    private final SystemEventPublisher systemEventPublisher;
    private final ObjectMapper objectMapper;

    public EdgeNodeSyncServiceImpl(ConfigSyncTaskRepository syncTaskRepository,
                                   EdgeNodeRepository edgeNodeRepository,
                                   AutoIrrigationConfigRepository autoIrrigationConfigRepository,
                                   MonitoringZoneRepository monitoringZoneRepository,
                                   DownlinkQueueService downlinkQueueService,
                                   @Autowired(required = false) SystemEventPublisher systemEventPublisher,
                                   ObjectMapper objectMapper) {
        this.syncTaskRepository = syncTaskRepository;
        this.edgeNodeRepository = edgeNodeRepository;
        this.autoIrrigationConfigRepository = autoIrrigationConfigRepository;
        this.monitoringZoneRepository = monitoringZoneRepository;
        this.downlinkQueueService = downlinkQueueService;
        this.systemEventPublisher = systemEventPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public ConfigSyncStatusResponse getSyncStatus(Long edgeNodeId) {
        log.info("Fetching config sync status for edgeNodeId: {}", edgeNodeId);
        EdgeNode node = edgeNodeRepository.findById(edgeNodeId)
                .orElseThrow(() -> new ResourceNotFoundException("EdgeNode not found with id: " + edgeNodeId));

        Optional<ConfigSyncTask> taskOpt = syncTaskRepository.findTopByEdgeNodeIdOrderByCreatedAtDesc(edgeNodeId);

        if (taskOpt.isPresent()) {
            ConfigSyncTask task = taskOpt.get();
            return ConfigSyncStatusResponse.builder()
                    .edgeNodeId(node.getId())
                    .nodeIdentifier(node.getNodeId())
                    .configVersion(task.getConfigVersion())
                    .status(task.getStatus())
                    .correlationId(task.getCorrelationId())
                    .retryCount(task.getRetryCount())
                    .failureReason(task.getFailureReason())
                    .lastSyncedAt(task.getLastSyncedAt())
                    .updatedAt(task.getUpdatedAt())
                    .build();
        }

        // Return default status if no task exists yet
        return ConfigSyncStatusResponse.builder()
                .edgeNodeId(node.getId())
                .nodeIdentifier(node.getNodeId())
                .configVersion(1L)
                .status(ConfigSyncStatus.PENDING)
                .retryCount(0)
                .build();
    }

    @Override
    public ConfigSyncStatusResponse triggerSyncForNode(Long edgeNodeId, String reason) {
        log.info("Triggering config sync for edgeNodeId: {}, reason: {}", edgeNodeId, reason);
        EdgeNode node = edgeNodeRepository.findById(edgeNodeId)
                .orElseThrow(() -> new ResourceNotFoundException("EdgeNode not found with id: " + edgeNodeId));

        Long fieldId = null;
        if (node.getMonitoringZone() != null && node.getMonitoringZone().getField() != null) {
            fieldId = node.getMonitoringZone().getField().getId();
        }

        AutoIrrigationConfig config = null;
        if (fieldId != null) {
            config = autoIrrigationConfigRepository.findByFieldId(fieldId).orElse(null);
        }

        Long version = config != null && config.getConfigVersion() != null ? config.getConfigVersion() : 1L;
        String correlationId = UUID.randomUUID().toString();

        ConfigSyncTask task = ConfigSyncTask.builder()
                .edgeNode(node)
                .configVersion(version)
                .correlationId(correlationId)
                .status(ConfigSyncStatus.QUEUED)
                .retryCount(0)
                .maxRetries(3)
                .build();

        ConfigSyncTask savedTask = syncTaskRepository.save(task);

        String payloadJson;
        try {
            AutoIrrigationConfigResponse responseDto = DtoMapper.toAutoIrrigationConfigResponse(config);
            payloadJson = objectMapper.writeValueAsString(responseDto != null ? responseDto : config);
        } catch (Exception e) {
            payloadJson = "{\"configVersion\":" + version + ",\"correlationId\":\"" + correlationId + "\"}";
        }

        downlinkQueueService.queueDownlink(node, payloadJson, correlationId);

        if (systemEventPublisher != null) {
            try {
                systemEventPublisher.publish(SystemEvent.builder()
                        .eventType(SystemEventType.CONFIG_SYNCED)
                        .fieldId(fieldId)
                        .aggregateId(node.getNodeId())
                        .payload(savedTask)
                        .build());
            } catch (Exception e) {
                log.warn("Failed to publish CONFIG_SYNCED event: {}", e.getMessage());
            }
        }

        return ConfigSyncStatusResponse.builder()
                .edgeNodeId(node.getId())
                .nodeIdentifier(node.getNodeId())
                .configVersion(version)
                .status(ConfigSyncStatus.QUEUED)
                .correlationId(correlationId)
                .retryCount(0)
                .updatedAt(savedTask.getUpdatedAt())
                .build();
    }

    @Override
    public void syncConfigForField(Long fieldId) {
        log.info("Triggering field-wide config sync for fieldId: {}", fieldId);
        List<MonitoringZone> zones = monitoringZoneRepository.findByFieldId(fieldId);

        for (MonitoringZone zone : zones) {
            List<EdgeNode> nodes = edgeNodeRepository.findByMonitoringZoneId(zone.getId());
            for (EdgeNode node : nodes) {
                try {
                    triggerSyncForNode(node.getId(), "Field configuration update auto-trigger");
                } catch (Exception e) {
                    log.error("Failed to trigger config sync for node {}: {}", node.getId(), e.getMessage());
                }
            }
        }
    }

    @Override
    public void processAcknowledgement(String correlationId) {
        log.info("Processing acknowledgement for correlationId: {}", correlationId);
        Optional<ConfigSyncTask> taskOpt = syncTaskRepository.findByCorrelationId(correlationId);
        if (taskOpt.isPresent()) {
            ConfigSyncTask task = taskOpt.get();
            task.setStatus(ConfigSyncStatus.ACKNOWLEDGED);
            task.setLastSyncedAt(LocalDateTime.now());
            syncTaskRepository.save(task);

            downlinkQueueService.handleAcknowledgement(correlationId);

            if (systemEventPublisher != null) {
                try {
                    systemEventPublisher.publish(SystemEvent.builder()
                            .eventType(SystemEventType.CONFIG_SYNCED)
                            .aggregateId(task.getEdgeNode().getNodeId())
                            .payload(task)
                            .build());
                } catch (Exception e) {
                    log.warn("Failed to publish ACK CONFIG_SYNCED event: {}", e.getMessage());
                }
            }
        }
    }

    @Override
    @Scheduled(fixedDelay = 60000)
    public void checkTaskTimeouts() {
        List<ConfigSyncTask> pendingTasks = syncTaskRepository.findAll().stream()
                .filter(t -> t.getStatus() == ConfigSyncStatus.QUEUED || t.getStatus() == ConfigSyncStatus.PENDING)
                .toList();

        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(5);
        for (ConfigSyncTask task : pendingTasks) {
            if (task.getCreatedAt() != null && task.getCreatedAt().isBefore(cutoff)) {
                if (task.getRetryCount() < task.getMaxRetries()) {
                    task.setRetryCount(task.getRetryCount() + 1);
                    syncTaskRepository.save(task);
                } else {
                    log.warn("Config sync task {} exceeded max retries, setting to TIMEOUT/FAILED", task.getCorrelationId());
                    task.setStatus(ConfigSyncStatus.TIMEOUT);
                    task.setFailureReason("Downlink delivery timeout without ACK");
                    syncTaskRepository.save(task);

                    if (systemEventPublisher != null) {
                        try {
                            systemEventPublisher.publish(SystemEvent.builder()
                                    .eventType(SystemEventType.NODE_STATUS_CHANGED)
                                    .aggregateId(task.getEdgeNode().getNodeId())
                                    .payload("CONFIG_SYNC_FAILED: Downlink timeout for node " + task.getEdgeNode().getNodeId())
                                    .build());
                        } catch (Exception e) {
                            log.warn("Failed to publish timeout NODE_STATUS_CHANGED event: {}", e.getMessage());
                        }
                    }
                }
            }
        }
    }
}

