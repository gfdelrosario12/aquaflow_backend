package com.aquaflow.backend.domain;

import com.aquaflow.backend.entity.ConfigSyncStatus;
import com.aquaflow.backend.entity.DownlinkQueueItem;
import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.infrastructure.lora.LoraNetworkServerClient;
import com.aquaflow.backend.infrastructure.lora.model.DownlinkConfirmationMode;
import com.aquaflow.backend.infrastructure.lora.model.LoraDownlinkMessage;
import com.aquaflow.backend.infrastructure.lora.model.LoraDownlinkResult;
import com.aquaflow.backend.persistence.DownlinkQueueItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DownlinkQueueServiceImpl implements DownlinkQueueService {

    private static final Logger log = LoggerFactory.getLogger(DownlinkQueueServiceImpl.class);

    private final DownlinkQueueItemRepository queueItemRepository;
    private final LoraNetworkServerClient loraNetworkServerClient;

    public DownlinkQueueServiceImpl(DownlinkQueueItemRepository queueItemRepository,
                                   @Autowired(required = false) LoraNetworkServerClient loraNetworkServerClient) {
        this.queueItemRepository = queueItemRepository;
        this.loraNetworkServerClient = loraNetworkServerClient;
    }

    @Override
    public DownlinkQueueItem queueDownlink(EdgeNode edgeNode, String payloadJson, String correlationId) {
        log.info("Queueing downlink for edgeNode: {}, correlationId: {}", edgeNode.getNodeId(), correlationId);

        // Mark previous pending queue items for this node as FAILED / superseded
        List<DownlinkQueueItem> existingPending = queueItemRepository.findByEdgeNodeIdAndStatus(edgeNode.getId(), ConfigSyncStatus.QUEUED);
        for (DownlinkQueueItem item : existingPending) {
            item.setStatus(ConfigSyncStatus.FAILED);
            queueItemRepository.save(item);
        }

        DownlinkQueueItem item = DownlinkQueueItem.builder()
                .edgeNode(edgeNode)
                .correlationId(correlationId)
                .payloadJson(payloadJson)
                .fPort(10)
                .confirmed(true)
                .retryCount(0)
                .maxRetries(3)
                .status(ConfigSyncStatus.QUEUED)
                .lastAttemptAt(LocalDateTime.now())
                .build();

        DownlinkQueueItem savedItem = queueItemRepository.save(item);
        transmitDownlink(savedItem);
        return savedItem;
    }

    @Override
    public boolean transmitDownlink(DownlinkQueueItem item) {
        if (loraNetworkServerClient == null) {
            log.warn("No LoraNetworkServerClient available to send downlink for correlationId: {}", item.getCorrelationId());
            item.setLastAttemptAt(LocalDateTime.now());
            queueItemRepository.save(item);
            return false;
        }

        try {
            String base64Payload = Base64.getEncoder().encodeToString(item.getPayloadJson().getBytes(StandardCharsets.UTF_8));

            String devEui = null;
            if (item.getEdgeNode() != null && item.getEdgeNode().getIdentity() != null) {
                devEui = item.getEdgeNode().getIdentity().getIdentityValue();
            }

            LoraDownlinkMessage message = LoraDownlinkMessage.builder()
                    .devEui(devEui)
                    .nodeId(item.getEdgeNode() != null ? item.getEdgeNode().getNodeId() : null)
                    .fPort(item.getFPort())
                    .payloadBase64(base64Payload)
                    .confirmationMode(item.getConfirmed() ? DownlinkConfirmationMode.CONFIRMED : DownlinkConfirmationMode.UNCONFIRMED)
                    .correlationId(item.getCorrelationId())
                    .build();

            log.info("Transmitting downlink message to LoRaWAN network server for node: {}", item.getEdgeNode().getNodeId());
            LoraDownlinkResult result = loraNetworkServerClient.sendDownlink(message);

            item.setLastAttemptAt(LocalDateTime.now());
            if (result != null && result.isSuccess()) {
                log.info("Successfully dispatched downlink to network server for correlationId: {}", item.getCorrelationId());
                queueItemRepository.save(item);
                return true;
            } else {
                log.warn("Network server returned error for downlink correlationId {}: {}", item.getCorrelationId(), result != null ? result.getMessage() : "null result");
                queueItemRepository.save(item);
                return false;
            }
        } catch (Exception e) {
            log.error("Exception occurred while transmitting downlink for correlationId {}: {}", item.getCorrelationId(), e.getMessage());
            item.setLastAttemptAt(LocalDateTime.now());
            queueItemRepository.save(item);
            return false;
        }
    }

    @Override
    public void handleAcknowledgement(String correlationId) {
        log.info("Handling downlink acknowledgement for correlationId: {}", correlationId);
        Optional<DownlinkQueueItem> itemOpt = queueItemRepository.findByCorrelationId(correlationId);
        if (itemOpt.isPresent()) {
            DownlinkQueueItem item = itemOpt.get();
            item.setStatus(ConfigSyncStatus.ACKNOWLEDGED);
            queueItemRepository.save(item);
            log.info("DownlinkQueueItem {} marked as ACKNOWLEDGED", correlationId);
        } else {
            log.warn("DownlinkQueueItem not found for correlationId: {}", correlationId);
        }
    }

    @Override
    @Scheduled(fixedDelay = 60000) // Check every 60 seconds
    public void processTimeoutsAndRetries() {
        List<DownlinkQueueItem> queuedItems = queueItemRepository.findByStatus(ConfigSyncStatus.QUEUED);
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(5);

        for (DownlinkQueueItem item : queuedItems) {
            if (item.getLastAttemptAt() != null && item.getLastAttemptAt().isBefore(thresholdTime)) {
                if (item.getRetryCount() < item.getMaxRetries()) {
                    item.setRetryCount(item.getRetryCount() + 1);
                    log.info("Retrying queued downlink item {} (Attempt {}/{})", item.getCorrelationId(), item.getRetryCount(), item.getMaxRetries());
                    transmitDownlink(item);
                } else {
                    log.warn("Queued downlink item {} reached max retries ({}), marking as TIMEOUT/FAILED", item.getCorrelationId(), item.getMaxRetries());
                    item.setStatus(ConfigSyncStatus.TIMEOUT);
                    queueItemRepository.save(item);
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DownlinkQueueItem> getQueueForNode(Long edgeNodeId) {
        return queueItemRepository.findByEdgeNodeIdAndStatus(edgeNodeId, ConfigSyncStatus.QUEUED);
    }
}
