package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.IrrigationDecisionServiceImpl;
import com.aquaflow.backend.domain.IrrigationDecisionValidator;
import com.aquaflow.backend.dto.request.IrrigationDecisionRequest;
import com.aquaflow.backend.dto.response.FieldIrrigationStatusResponse;
import com.aquaflow.backend.dto.response.IrrigationDecisionResponse;
import com.aquaflow.backend.entity.CropGrowthStage;
import com.aquaflow.backend.entity.DecisionType;
import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.entity.Field;
import com.aquaflow.backend.entity.IrrigationDecision;
import com.aquaflow.backend.entity.MonitoringZone;
import com.aquaflow.backend.entity.TriggerReason;
import com.aquaflow.backend.infrastructure.event.SystemEvent;
import com.aquaflow.backend.infrastructure.event.SystemEventPublisher;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.persistence.EdgeNodeRepository;
import com.aquaflow.backend.persistence.FieldRepository;
import com.aquaflow.backend.persistence.IrrigationDecisionRepository;
import com.aquaflow.backend.persistence.MonitoringZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IrrigationDecisionServiceImplTest {

    @Mock
    private IrrigationDecisionRepository decisionRepository;

    @Mock
    private EdgeNodeRepository edgeNodeRepository;

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private MonitoringZoneRepository monitoringZoneRepository;

    @Mock
    private SystemEventPublisher systemEventPublisher;

    private IrrigationDecisionValidator validator;
    private IrrigationDecisionServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new IrrigationDecisionValidator();
        service = new IrrigationDecisionServiceImpl(
                decisionRepository,
                edgeNodeRepository,
                fieldRepository,
                monitoringZoneRepository,
                validator,
                systemEventPublisher
        );
    }

    @Test
    void shouldReportDecisionSuccessfully() {
        EdgeNode node = EdgeNode.builder().id(1L).nodeId("NODE-01").build();
        IrrigationDecisionRequest request = IrrigationDecisionRequest.builder()
                .edgeNodeId(1L)
                .decisionType(DecisionType.SOIL_MOISTURE_TRIGGER)
                .triggerReason(TriggerReason.SOIL_MOISTURE_BELOW_MIN)
                .cropStage(CropGrowthStage.VEGETATIVE)
                .confidence(0.90)
                .requestedDurationMinutes(45)
                .requestedVolumeLiters(300.0)
                .executionStatus("IN_PROGRESS")
                .build();

        when(edgeNodeRepository.findById(1L)).thenReturn(Optional.of(node));
        when(decisionRepository.save(any(IrrigationDecision.class))).thenAnswer(i -> {
            IrrigationDecision d = i.getArgument(0);
            d.setId(100L);
            return d;
        });

        IrrigationDecisionResponse response = service.reportDecision(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getEdgeNodeId()).isEqualTo(1L);
        assertThat(response.getDecisionType()).isEqualTo(DecisionType.SOIL_MOISTURE_TRIGGER);
        assertThat(response.getExecutionStatus()).isEqualTo("IN_PROGRESS");
        verify(systemEventPublisher, times(2)).publish(any(SystemEvent.class));
    }

    @Test
    void shouldGetFieldIrrigationStatus() {
        Field field = Field.builder().id(10L).name("South Field").build();
        MonitoringZone zone = MonitoringZone.builder().id(20L).field(field).build();
        EdgeNode node = EdgeNode.builder().id(1L).nodeId("NODE-01").monitoringZone(zone).build();
        IrrigationDecision decision = IrrigationDecision.builder()
                .id(100L)
                .edgeNode(node)
                .executionStatus("IN_PROGRESS")
                .requestedVolumeLiters(150.0)
                .nodeTimestamp(LocalDateTime.now())
                .build();

        when(fieldRepository.findById(10L)).thenReturn(Optional.of(field));
        when(monitoringZoneRepository.findByFieldId(10L)).thenReturn(List.of(zone));
        when(edgeNodeRepository.findByMonitoringZoneId(20L)).thenReturn(List.of(node));
        when(decisionRepository.findTop10ByEdgeNodeIdInOrderByNodeTimestampDesc(List.of(1L))).thenReturn(List.of(decision));
        when(decisionRepository.findByEdgeNodeIdInAndNodeTimestampAfter(eq(List.of(1L)), any())).thenReturn(List.of(decision));

        FieldIrrigationStatusResponse statusResponse = service.getFieldIrrigationStatus(10L);

        assertThat(statusResponse).isNotNull();
        assertThat(statusResponse.getFieldId()).isEqualTo(10L);
        assertThat(statusResponse.getIsIrrigating()).isTrue();
        assertThat(statusResponse.getActiveIrrigatingNodesCount()).isEqualTo(1);
        assertThat(statusResponse.getTodayDeliveredVolumeLiters()).isEqualTo(150.0);
        assertThat(statusResponse.getRecentDecisions()).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenFieldNotFound() {
        when(fieldRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getFieldIrrigationStatus(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

