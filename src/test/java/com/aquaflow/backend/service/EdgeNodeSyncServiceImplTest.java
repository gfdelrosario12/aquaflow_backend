package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.DownlinkQueueService;
import com.aquaflow.backend.domain.EdgeNodeSyncServiceImpl;
import com.aquaflow.backend.dto.response.ConfigSyncStatusResponse;
import com.aquaflow.backend.entity.AutoIrrigationConfig;
import com.aquaflow.backend.entity.ConfigSyncStatus;
import com.aquaflow.backend.entity.ConfigSyncTask;
import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.entity.Field;
import com.aquaflow.backend.entity.MonitoringZone;
import com.aquaflow.backend.infrastructure.event.SystemEventPublisher;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.persistence.AutoIrrigationConfigRepository;
import com.aquaflow.backend.persistence.ConfigSyncTaskRepository;
import com.aquaflow.backend.persistence.EdgeNodeRepository;
import com.aquaflow.backend.persistence.MonitoringZoneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EdgeNodeSyncServiceImplTest {

    @Mock
    private ConfigSyncTaskRepository syncTaskRepository;

    @Mock
    private EdgeNodeRepository edgeNodeRepository;

    @Mock
    private AutoIrrigationConfigRepository autoIrrigationConfigRepository;

    @Mock
    private MonitoringZoneRepository monitoringZoneRepository;

    @Mock
    private DownlinkQueueService downlinkQueueService;

    @Mock
    private SystemEventPublisher systemEventPublisher;

    private ObjectMapper objectMapper;
    private EdgeNodeSyncServiceImpl syncService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        syncService = new EdgeNodeSyncServiceImpl(
                syncTaskRepository,
                edgeNodeRepository,
                autoIrrigationConfigRepository,
                monitoringZoneRepository,
                downlinkQueueService,
                systemEventPublisher,
                objectMapper
        );
    }

    @Test
    void shouldGetSyncStatusForExistingNode() {
        EdgeNode node = EdgeNode.builder().id(1L).nodeId("NODE-01").build();
        ConfigSyncTask task = ConfigSyncTask.builder()
                .id(10L)
                .edgeNode(node)
                .configVersion(3L)
                .status(ConfigSyncStatus.ACKNOWLEDGED)
                .correlationId("corr-789")
                .build();

        when(edgeNodeRepository.findById(1L)).thenReturn(Optional.of(node));
        when(syncTaskRepository.findTopByEdgeNodeIdOrderByCreatedAtDesc(1L)).thenReturn(Optional.of(task));

        ConfigSyncStatusResponse response = syncService.getSyncStatus(1L);

        assertThat(response).isNotNull();
        assertThat(response.getEdgeNodeId()).isEqualTo(1L);
        assertThat(response.getConfigVersion()).isEqualTo(3L);
        assertThat(response.getStatus()).isEqualTo(ConfigSyncStatus.ACKNOWLEDGED);
    }

    @Test
    void shouldTriggerSyncForNodeAndQueueDownlink() {
        Field field = Field.builder().id(100L).name("Field 1").build();
        MonitoringZone zone = MonitoringZone.builder().id(200L).field(field).build();
        EdgeNode node = EdgeNode.builder().id(1L).nodeId("NODE-01").monitoringZone(zone).build();
        AutoIrrigationConfig config = AutoIrrigationConfig.builder().id(50L).field(field).configVersion(2L).build();

        when(edgeNodeRepository.findById(1L)).thenReturn(Optional.of(node));
        when(autoIrrigationConfigRepository.findByFieldId(100L)).thenReturn(Optional.of(config));
        when(syncTaskRepository.save(any(ConfigSyncTask.class))).thenAnswer(i -> i.getArgument(0));

        ConfigSyncStatusResponse response = syncService.triggerSyncForNode(1L, "Manual test trigger");

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(ConfigSyncStatus.QUEUED);
        assertThat(response.getConfigVersion()).isEqualTo(2L);
        verify(downlinkQueueService).queueDownlink(eq(node), anyString(), anyString());
    }

    @Test
    void shouldProcessAcknowledgementForTask() {
        EdgeNode node = EdgeNode.builder().id(1L).nodeId("NODE-01").build();
        ConfigSyncTask task = ConfigSyncTask.builder()
                .id(10L)
                .edgeNode(node)
                .correlationId("corr-abc")
                .status(ConfigSyncStatus.QUEUED)
                .build();

        when(syncTaskRepository.findByCorrelationId("corr-abc")).thenReturn(Optional.of(task));
        when(syncTaskRepository.save(any(ConfigSyncTask.class))).thenAnswer(i -> i.getArgument(0));

        syncService.processAcknowledgement("corr-abc");

        assertThat(task.getStatus()).isEqualTo(ConfigSyncStatus.ACKNOWLEDGED);
        verify(downlinkQueueService).handleAcknowledgement("corr-abc");
    }

    @Test
    void shouldThrowExceptionWhenNodeNotFound() {
        when(edgeNodeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> syncService.getSyncStatus(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

