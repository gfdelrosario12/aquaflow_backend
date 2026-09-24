package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.AuditQueryRequest;
import com.aquaflow.backend.dto.response.AuditExportResponse;
import com.aquaflow.backend.dto.response.IrrigationAuditLogResponse;
import com.aquaflow.backend.dto.response.SystemAuditLogResponse;
import com.aquaflow.backend.entity.IrrigationAuditLog;
import com.aquaflow.backend.entity.SystemAuditLog;
import com.aquaflow.backend.persistence.IrrigationAuditLogRepository;
import com.aquaflow.backend.persistence.SystemAuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    private final SystemAuditLogRepository systemAuditLogRepository;
    private final IrrigationAuditLogRepository irrigationAuditLogRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuditLogServiceImpl(SystemAuditLogRepository systemAuditLogRepository,
                               IrrigationAuditLogRepository irrigationAuditLogRepository,
                               ObjectMapper objectMapper) {
        this.systemAuditLogRepository = systemAuditLogRepository;
        this.irrigationAuditLogRepository = irrigationAuditLogRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public SystemAuditLog logSystemEvent(String eventType, String actor, String entityType, String entityId, String correlationId, String previousState, String resultingState, Object payload) {
        String jsonPayload = null;
        if (payload != null) {
            try {
                jsonPayload = payload instanceof String ? (String) payload : objectMapper.writeValueAsString(payload);
            } catch (Exception e) {
                jsonPayload = payload.toString();
            }
        }

        SystemAuditLog logEntry = SystemAuditLog.builder()
                .eventType(eventType)
                .actor(actor != null ? actor : "SYSTEM")
                .entityType(entityType != null ? entityType : "SYSTEM")
                .entityId(entityId != null ? entityId : "UNKNOWN")
                .correlationId(correlationId)
                .previousState(previousState)
                .resultingState(resultingState)
                .payloadJson(jsonPayload)
                .createdAt(LocalDateTime.now())
                .build();

        return systemAuditLogRepository.save(logEntry);
    }

    @Override
    public IrrigationAuditLog logIrrigationEvent(String eventType, String actor, String entityType, String entityId, String correlationId, Object payload) {
        String jsonPayload = null;
        if (payload != null) {
            try {
                jsonPayload = payload instanceof String ? (String) payload : objectMapper.writeValueAsString(payload);
            } catch (Exception e) {
                jsonPayload = payload.toString();
            }
        }

        IrrigationAuditLog logEntry = IrrigationAuditLog.builder()
                .eventType(eventType)
                .actor(actor != null ? actor : "SYSTEM")
                .entityType(entityType != null ? entityType : "IRRIGATION")
                .entityId(entityId != null ? entityId : "UNKNOWN")
                .correlationId(correlationId)
                .payloadJson(jsonPayload)
                .createdAt(LocalDateTime.now())
                .build();

        return irrigationAuditLogRepository.save(logEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IrrigationAuditLogResponse> getIrrigationAuditLogs(AuditQueryRequest queryRequest, Pageable pageable) {
        log.info("Querying irrigation audit logs with request: {}", queryRequest);

        Page<IrrigationAuditLog> page;
        if (queryRequest != null && queryRequest.getCorrelationId() != null && !queryRequest.getCorrelationId().isBlank()) {
            page = irrigationAuditLogRepository.findByCorrelationId(queryRequest.getCorrelationId(), pageable);
        } else if (queryRequest != null && queryRequest.getEventType() != null && !queryRequest.getEventType().isBlank()) {
            page = irrigationAuditLogRepository.findByEventType(queryRequest.getEventType(), pageable);
        } else if (queryRequest != null && queryRequest.getActor() != null && !queryRequest.getActor().isBlank()) {
            page = irrigationAuditLogRepository.findByActor(queryRequest.getActor(), pageable);
        } else if (queryRequest != null && queryRequest.getEntityType() != null && queryRequest.getEntityId() != null) {
            page = irrigationAuditLogRepository.findByEntityTypeAndEntityId(queryRequest.getEntityType(), queryRequest.getEntityId(), pageable);
        } else if (queryRequest != null && queryRequest.getFrom() != null && queryRequest.getTo() != null) {
            page = irrigationAuditLogRepository.findByCreatedAtBetween(queryRequest.getFrom(), queryRequest.getTo(), pageable);
        } else {
            page = irrigationAuditLogRepository.findAll(pageable);
        }

        List<IrrigationAuditLogResponse> dtos = page.getContent().stream()
                .map(this::mapToIrrigationResponse)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemAuditLogResponse> getSystemAuditLogs(AuditQueryRequest queryRequest, Pageable pageable) {
        log.info("Querying system audit logs with request: {}", queryRequest);

        Page<SystemAuditLog> page;
        if (queryRequest != null && queryRequest.getCorrelationId() != null && !queryRequest.getCorrelationId().isBlank()) {
            page = systemAuditLogRepository.findByCorrelationId(queryRequest.getCorrelationId(), pageable);
        } else if (queryRequest != null && queryRequest.getEventType() != null && !queryRequest.getEventType().isBlank()) {
            page = systemAuditLogRepository.findByEventType(queryRequest.getEventType(), pageable);
        } else if (queryRequest != null && queryRequest.getActor() != null && !queryRequest.getActor().isBlank()) {
            page = systemAuditLogRepository.findByActor(queryRequest.getActor(), pageable);
        } else if (queryRequest != null && queryRequest.getEntityType() != null && queryRequest.getEntityId() != null) {
            page = systemAuditLogRepository.findByEntityTypeAndEntityId(queryRequest.getEntityType(), queryRequest.getEntityId(), pageable);
        } else if (queryRequest != null && queryRequest.getFrom() != null && queryRequest.getTo() != null) {
            page = systemAuditLogRepository.findByCreatedAtBetween(queryRequest.getFrom(), queryRequest.getTo(), pageable);
        } else {
            page = systemAuditLogRepository.findAll(pageable);
        }

        List<SystemAuditLogResponse> dtos = page.getContent().stream()
                .map(this::mapToSystemResponse)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public AuditExportResponse exportAuditLogs(AuditQueryRequest queryRequest) {
        log.info("Exporting audit logs with filter: {}", queryRequest);

        String format = (queryRequest != null && queryRequest.getFormat() != null && queryRequest.getFormat().equalsIgnoreCase("csv"))
                ? "csv" : "json";

        List<IrrigationAuditLog> irrigationLogs;
        List<SystemAuditLog> systemLogs;

        if (queryRequest != null && queryRequest.getFrom() != null && queryRequest.getTo() != null) {
            irrigationLogs = irrigationAuditLogRepository.findByCreatedAtBetween(queryRequest.getFrom(), queryRequest.getTo());
            systemLogs = systemAuditLogRepository.findByCreatedAtBetween(queryRequest.getFrom(), queryRequest.getTo());
        } else {
            irrigationLogs = irrigationAuditLogRepository.findAll();
            systemLogs = systemAuditLogRepository.findAll();
        }

        int totalRecords = irrigationLogs.size() + systemLogs.size();
        String content;

        if ("csv".equalsIgnoreCase(format)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Category,Id,EventType,Actor,EntityType,EntityId,CorrelationId,PreviousState,ResultingState,CreatedAt\n");
            for (IrrigationAuditLog item : irrigationLogs) {
                sb.append(String.format("IRRIGATION,%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"\",\"\",\"%s\"\n",
                        item.getId(),
                        escapeCsv(item.getEventType()),
                        escapeCsv(item.getActor()),
                        escapeCsv(item.getEntityType()),
                        escapeCsv(item.getEntityId()),
                        escapeCsv(item.getCorrelationId()),
                        item.getCreatedAt()));
            }
            for (SystemAuditLog item : systemLogs) {
                sb.append(String.format("SYSTEM,%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        item.getId(),
                        escapeCsv(item.getEventType()),
                        escapeCsv(item.getActor()),
                        escapeCsv(item.getEntityType()),
                        escapeCsv(item.getEntityId()),
                        escapeCsv(item.getCorrelationId()),
                        escapeCsv(item.getPreviousState()),
                        escapeCsv(item.getResultingState()),
                        item.getCreatedAt()));
            }
            content = sb.toString();
        } else {
            try {
                List<Object> combined = new ArrayList<>();
                combined.addAll(irrigationLogs.stream().map(this::mapToIrrigationResponse).toList());
                combined.addAll(systemLogs.stream().map(this::mapToSystemResponse).toList());
                content = objectMapper.writeValueAsString(combined);
            } catch (Exception e) {
                content = "[]";
            }
        }

        return AuditExportResponse.builder()
                .format(format)
                .recordCount(totalRecords)
                .content(content)
                .exportedAt(LocalDateTime.now())
                .build();
    }

    private IrrigationAuditLogResponse mapToIrrigationResponse(IrrigationAuditLog log) {
        return IrrigationAuditLogResponse.builder()
                .id(log.getId())
                .eventType(log.getEventType())
                .actor(log.getActor())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .correlationId(log.getCorrelationId())
                .payloadJson(log.getPayloadJson())
                .createdAt(log.getCreatedAt())
                .build();
    }

    private SystemAuditLogResponse mapToSystemResponse(SystemAuditLog log) {
        return SystemAuditLogResponse.builder()
                .id(log.getId())
                .eventType(log.getEventType())
                .actor(log.getActor())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .correlationId(log.getCorrelationId())
                .previousState(log.getPreviousState())
                .resultingState(log.getResultingState())
                .payloadJson(log.getPayloadJson())
                .createdAt(log.getCreatedAt())
                .build();
    }

    private String escapeCsv(String input) {
        if (input == null) return "";
        return input.replace("\"", "\"\"");
    }
}
