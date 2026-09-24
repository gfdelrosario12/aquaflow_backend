package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.IrrigationDecisionRequest;
import com.aquaflow.backend.dto.response.FieldIrrigationStatusResponse;
import com.aquaflow.backend.dto.response.IrrigationDecisionResponse;
import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.entity.Field;
import com.aquaflow.backend.entity.IrrigationDecision;
import com.aquaflow.backend.entity.MonitoringZone;
import com.aquaflow.backend.infrastructure.event.SystemEvent;
import com.aquaflow.backend.infrastructure.event.SystemEventPublisher;
import com.aquaflow.backend.infrastructure.event.SystemEventType;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.infrastructure.util.DtoMapper;
import com.aquaflow.backend.persistence.EdgeNodeRepository;
import com.aquaflow.backend.persistence.FieldRepository;
import com.aquaflow.backend.persistence.IrrigationDecisionRepository;
import com.aquaflow.backend.persistence.MonitoringZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class IrrigationDecisionServiceImpl implements IrrigationDecisionService {

    private static final Logger log = LoggerFactory.getLogger(IrrigationDecisionServiceImpl.class);

    private final IrrigationDecisionRepository decisionRepository;
    private final EdgeNodeRepository edgeNodeRepository;
    private final FieldRepository fieldRepository;
    private final MonitoringZoneRepository monitoringZoneRepository;
    private final IrrigationDecisionValidator validator;
    private final SystemEventPublisher systemEventPublisher;

    @Autowired
    public IrrigationDecisionServiceImpl(IrrigationDecisionRepository decisionRepository,
                                         EdgeNodeRepository edgeNodeRepository,
                                         FieldRepository fieldRepository,
                                         MonitoringZoneRepository monitoringZoneRepository,
                                         IrrigationDecisionValidator validator,
                                         @Autowired(required = false) SystemEventPublisher systemEventPublisher) {
        this.decisionRepository = decisionRepository;
        this.edgeNodeRepository = edgeNodeRepository;
        this.fieldRepository = fieldRepository;
        this.monitoringZoneRepository = monitoringZoneRepository;
        this.validator = validator;
        this.systemEventPublisher = systemEventPublisher;
    }

    @Override
    public IrrigationDecisionResponse reportDecision(IrrigationDecisionRequest request) {
        log.info("Ingesting autonomous irrigation decision from edge node");
        validator.validate(request);

        EdgeNode node = resolveEdgeNode(request);

        IrrigationDecision decision = IrrigationDecision.builder()
                .edgeNode(node)
                .decisionType(request.getDecisionType())
                .triggerReason(request.getTriggerReason())
                .cropStage(request.getCropStage())
                .confidence(request.getConfidence())
                .requestedDurationMinutes(request.getRequestedDurationMinutes())
                .requestedVolumeLiters(request.getRequestedVolumeLiters())
                .actualDurationMinutes(request.getActualDurationMinutes())
                .executionStatus(request.getExecutionStatus() != null ? request.getExecutionStatus() : "COMPLETED")
                .failureReason(request.getFailureReason())
                .configVersion(request.getConfigVersion())
                .correlationId(request.getCorrelationId())
                .telemetryWaterLevelCm(request.getTelemetryWaterLevelCm())
                .telemetrySoilMoisturePercent(request.getTelemetrySoilMoisturePercent())
                .telemetryTemperatureC(request.getTelemetryTemperatureC())
                .batteryPercentage(request.getBatteryPercentage())
                .rssi(request.getRssi())
                .snr(request.getSnr())
                .nodeTimestamp(request.getNodeTimestamp() != null ? request.getNodeTimestamp() : LocalDateTime.now())
                .build();

        IrrigationDecision saved = decisionRepository.save(decision);
        IrrigationDecisionResponse response = DtoMapper.toIrrigationDecisionResponse(saved);

        if (systemEventPublisher != null) {
            try {
                Long fieldId = (node.getMonitoringZone() != null && node.getMonitoringZone().getField() != null)
                        ? node.getMonitoringZone().getField().getId() : null;

                systemEventPublisher.publish(SystemEvent.builder()
                        .eventType(SystemEventType.IRRIGATION_DECISION_MADE)
                        .fieldId(fieldId)
                        .aggregateId(saved.getId().toString())
                        .payload(response)
                        .build());

                if ("IN_PROGRESS".equalsIgnoreCase(saved.getExecutionStatus()) || "COMPLETED".equalsIgnoreCase(saved.getExecutionStatus())) {
                    systemEventPublisher.publish(SystemEvent.builder()
                            .eventType(SystemEventType.IRRIGATION_EXECUTION_UPDATED)
                            .fieldId(fieldId)
                            .aggregateId(saved.getId().toString())
                            .payload(response)
                            .build());
                }
            } catch (Exception e) {
                log.warn("Failed to publish decision events: {}", e.getMessage());
            }
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IrrigationDecisionResponse> getDecisions(Long edgeNodeId, String executionStatus, Pageable pageable) {
        log.info("Querying irrigation decisions with edgeNodeId: {}, status: {}", edgeNodeId, executionStatus);

        Page<IrrigationDecision> decisions;
        if (edgeNodeId != null) {
            decisions = decisionRepository.findByEdgeNodeId(edgeNodeId, pageable);
        } else if (executionStatus != null && !executionStatus.isBlank()) {
            decisions = decisionRepository.findByExecutionStatus(executionStatus, pageable);
        } else {
            decisions = decisionRepository.findAll(pageable);
        }

        return decisions.map(DtoMapper::toIrrigationDecisionResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public IrrigationDecisionResponse getDecisionById(Long id) {
        log.info("Fetching irrigation decision by id: {}", id);
        IrrigationDecision decision = decisionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IrrigationDecision not found with id: " + id));
        return DtoMapper.toIrrigationDecisionResponse(decision);
    }

    @Override
    @Transactional(readOnly = true)
    public FieldIrrigationStatusResponse getFieldIrrigationStatus(Long fieldId) {
        log.info("Fetching active irrigation status for fieldId: {}", fieldId);

        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + fieldId));

        List<MonitoringZone> zones = monitoringZoneRepository.findByFieldId(fieldId);
        List<Long> edgeNodeIds = new ArrayList<>();
        for (MonitoringZone zone : zones) {
            List<EdgeNode> nodes = edgeNodeRepository.findByMonitoringZoneId(zone.getId());
            for (EdgeNode node : nodes) {
                edgeNodeIds.add(node.getId());
            }
        }

        if (edgeNodeIds.isEmpty()) {
            return FieldIrrigationStatusResponse.builder()
                    .fieldId(field.getId())
                    .fieldName(field.getName())
                    .isIrrigating(false)
                    .activeIrrigatingNodesCount(0)
                    .todayDeliveredVolumeLiters(0.0)
                    .lastUpdated(LocalDateTime.now())
                    .recentDecisions(new ArrayList<>())
                    .build();
        }

        List<IrrigationDecision> recentDecisions = decisionRepository.findTop10ByEdgeNodeIdInOrderByNodeTimestampDesc(edgeNodeIds);
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<IrrigationDecision> todayDecisions = decisionRepository.findByEdgeNodeIdInAndNodeTimestampAfter(edgeNodeIds, startOfDay);

        int activeNodes = 0;
        double todayVolume = 0.0;

        for (IrrigationDecision d : todayDecisions) {
            if ("IN_PROGRESS".equalsIgnoreCase(d.getExecutionStatus())) {
                activeNodes++;
            }
            if (d.getRequestedVolumeLiters() != null) {
                todayVolume += d.getRequestedVolumeLiters();
            }
        }

        List<IrrigationDecisionResponse> decisionResponses = recentDecisions.stream()
                .map(DtoMapper::toIrrigationDecisionResponse)
                .toList();

        return FieldIrrigationStatusResponse.builder()
                .fieldId(field.getId())
                .fieldName(field.getName())
                .isIrrigating(activeNodes > 0)
                .activeIrrigatingNodesCount(activeNodes)
                .todayDeliveredVolumeLiters(todayVolume)
                .lastUpdated(LocalDateTime.now())
                .recentDecisions(decisionResponses)
                .build();
    }

    private EdgeNode resolveEdgeNode(IrrigationDecisionRequest request) {
        if (request.getEdgeNodeId() != null) {
            return edgeNodeRepository.findById(request.getEdgeNodeId())
                    .orElseThrow(() -> new ResourceNotFoundException("EdgeNode not found with id: " + request.getEdgeNodeId()));
        }
        if (request.getNodeId() != null) {
            return edgeNodeRepository.findByNodeId(request.getNodeId())
                    .orElseThrow(() -> new ResourceNotFoundException("EdgeNode not found with nodeId: " + request.getNodeId()));
        }
        throw new ResourceNotFoundException("EdgeNode identity missing from request");
    }
}

