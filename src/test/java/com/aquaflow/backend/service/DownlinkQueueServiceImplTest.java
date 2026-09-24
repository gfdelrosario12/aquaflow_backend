package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.DownlinkQueueServiceImpl;
import com.aquaflow.backend.entity.CommunicationIdentity;
import com.aquaflow.backend.entity.ConfigSyncStatus;
import com.aquaflow.backend.entity.DownlinkQueueItem;
import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.infrastructure.lora.LoraNetworkServerClient;
import com.aquaflow.backend.infrastructure.lora.model.LoraDownlinkResult;
import com.aquaflow.backend.persistence.DownlinkQueueItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DownlinkQueueServiceImplTest {

    @Mock
    private DownlinkQueueItemRepository queueItemRepository;

    @Mock
    private LoraNetworkServerClient loraNetworkServerClient;

    private DownlinkQueueServiceImpl downlinkQueueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        downlinkQueueService = new DownlinkQueueServiceImpl(queueItemRepository, loraNetworkServerClient);
    }

    @Test
    void shouldQueueAndTransmitDownlinkSuccessfully() {
        CommunicationIdentity identity = CommunicationIdentity.builder().identityValue("0011223344556677").build();
        EdgeNode node = EdgeNode.builder().id(1L).nodeId("NODE-01").identity(identity).build();
        when(queueItemRepository.findByEdgeNodeIdAndStatus(1L, ConfigSyncStatus.QUEUED)).thenReturn(List.of());
        when(queueItemRepository.save(any(DownlinkQueueItem.class))).thenAnswer(i -> i.getArgument(0));
        when(loraNetworkServerClient.sendDownlink(any())).thenReturn(LoraDownlinkResult.builder().success(true).build());

        DownlinkQueueItem item = downlinkQueueService.queueDownlink(node, "{\"configVersion\":1}", "corr-123");

        assertThat(item).isNotNull();
        assertThat(item.getCorrelationId()).isEqualTo("corr-123");
        assertThat(item.getStatus()).isEqualTo(ConfigSyncStatus.QUEUED);
        verify(loraNetworkServerClient).sendDownlink(any());
    }

    @Test
    void shouldHandleAcknowledgementSuccessfully() {
        DownlinkQueueItem item = DownlinkQueueItem.builder()
                .id(10L)
                .correlationId("corr-123")
                .status(ConfigSyncStatus.QUEUED)
                .build();

        when(queueItemRepository.findByCorrelationId("corr-123")).thenReturn(Optional.of(item));
        when(queueItemRepository.save(any(DownlinkQueueItem.class))).thenAnswer(i -> i.getArgument(0));

        downlinkQueueService.handleAcknowledgement("corr-123");

        assertThat(item.getStatus()).isEqualTo(ConfigSyncStatus.ACKNOWLEDGED);
    }

    @Test
    void shouldRetryTimedOutQueueItems() {
        CommunicationIdentity identity = CommunicationIdentity.builder().identityValue("00112233").build();
        DownlinkQueueItem timedOutItem = DownlinkQueueItem.builder()
                .id(1L)
                .correlationId("corr-timeout")
                .status(ConfigSyncStatus.QUEUED)
                .retryCount(0)
                .maxRetries(3)
                .lastAttemptAt(LocalDateTime.now().minusMinutes(10))
                .edgeNode(EdgeNode.builder().id(1L).nodeId("NODE-01").identity(identity).build())
                .payloadJson("{}")
                .build();

        when(queueItemRepository.findByStatus(ConfigSyncStatus.QUEUED)).thenReturn(List.of(timedOutItem));
        when(loraNetworkServerClient.sendDownlink(any())).thenReturn(LoraDownlinkResult.builder().success(true).build());

        downlinkQueueService.processTimeoutsAndRetries();

        assertThat(timedOutItem.getRetryCount()).isEqualTo(1);
        verify(loraNetworkServerClient).sendDownlink(any());
    }
}

