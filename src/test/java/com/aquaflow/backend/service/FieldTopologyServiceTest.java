package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.FieldTopologyServiceImpl;
import com.aquaflow.backend.dto.response.FieldTopologyResponse;
import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.entity.Field;
import com.aquaflow.backend.entity.MonitoringPoint;
import com.aquaflow.backend.entity.MonitoringZone;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.aquaflow.backend.persistence.EdgeNodeRepository;
import com.aquaflow.backend.persistence.FieldRepository;
import com.aquaflow.backend.persistence.MonitoringPointRepository;
import com.aquaflow.backend.persistence.MonitoringZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class FieldTopologyServiceTest {

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private MonitoringZoneRepository monitoringZoneRepository;

    @Mock
    private MonitoringPointRepository monitoringPointRepository;

    @Mock
    private EdgeNodeRepository edgeNodeRepository;

    @Mock
    private com.aquaflow.backend.domain.ZoneAggregationService zoneAggregationService;

    private FieldTopologyServiceImpl topologyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        topologyService = new FieldTopologyServiceImpl(
                fieldRepository,
                monitoringZoneRepository,
                monitoringPointRepository,
                edgeNodeRepository,
                zoneAggregationService
        );
    }

    @Test
    void shouldFetchCompleteFieldTopologyTree() {
        Field field = Field.builder().id(1L).name("South Field").areaHectares(20.0).build();
        MonitoringZone zone = MonitoringZone.builder().id(2L).name("Zone A").field(field).cropType("RICE").build();
        MonitoringPoint point = MonitoringPoint.builder().id(3L).name("Point 1").monitoringZone(zone).build();
        EdgeNode node = EdgeNode.builder().id(4L).nodeId("NODE-01").monitoringZone(zone).build();

        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
        when(monitoringZoneRepository.findByFieldId(1L)).thenReturn(List.of(zone));
        when(monitoringPointRepository.findByMonitoringZoneId(2L)).thenReturn(List.of(point));
        when(edgeNodeRepository.findByMonitoringZoneId(2L)).thenReturn(List.of(node));

        FieldTopologyResponse topology = topologyService.getFieldTopology(1L);

        assertThat(topology).isNotNull();
        assertThat(topology.getId()).isEqualTo(1L);
        assertThat(topology.getZones()).hasSize(1);
        assertThat(topology.getZones().get(0).getMonitoringPoints()).hasSize(1);
        assertThat(topology.getZones().get(0).getAssignedNodes()).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenFieldNotFound() {
        when(fieldRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> topologyService.getFieldTopology(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldValidateNodeAssignmentCrossZoneMismatch() {
        MonitoringZone zone1 = MonitoringZone.builder().id(1L).name("Zone 1").build();
        MonitoringZone zone2 = MonitoringZone.builder().id(2L).name("Zone 2").build();

        EdgeNode node = EdgeNode.builder().id(100L).monitoringZone(zone1).build();

        when(edgeNodeRepository.findById(100L)).thenReturn(Optional.of(node));
        when(monitoringZoneRepository.findById(2L)).thenReturn(Optional.of(zone2));

        assertThatThrownBy(() -> topologyService.validateNodeAssignmentToZone(100L, 2L))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("already assigned to a different monitoring zone");
    }
}

