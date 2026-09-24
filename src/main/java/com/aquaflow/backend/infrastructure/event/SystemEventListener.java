package com.aquaflow.backend.infrastructure.event;

import com.aquaflow.backend.domain.AuditLogService;
import com.aquaflow.backend.infrastructure.web.RealtimeEventPublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SystemEventListener {

    private static final Logger log = LoggerFactory.getLogger(SystemEventListener.class);

    private final RealtimeEventPublisherService realtimeEventPublisherService;
    private final AuditLogService auditLogService;

    @Autowired
    public SystemEventListener(RealtimeEventPublisherService realtimeEventPublisherService,
                               @Autowired(required = false) AuditLogService auditLogService) {
        this.realtimeEventPublisherService = realtimeEventPublisherService;
        this.auditLogService = auditLogService;
    }

    @Async
    @EventListener
    public void handleSystemEvent(SystemEvent event) {
        log.debug("Received system event for async dispatch: {} ({})", event.getEventType(), event.getEventId());
        try {
            realtimeEventPublisherService.broadcastEvent(event);
        } catch (Exception e) {
            log.error("Error in async SystemEventListener broadcasting event {}: {}", event.getEventId(), e.getMessage());
        }

        if (auditLogService != null && event.getEventType() != null) {
            try {
                if (event.getEventType() == SystemEventType.NODE_STATUS_CHANGED
                        || event.getEventType() == SystemEventType.ALARM_TRIGGERED
                        || event.getEventType() == SystemEventType.CONFIG_SYNCED) {
                    auditLogService.logSystemEvent(
                            event.getEventType().name(),
                            "SYSTEM",
                            "NODE",
                            event.getAggregateId() != null ? event.getAggregateId() : "NODE",
                            event.getAggregateId(),
                            null,
                            event.getEventType().name(),
                            event.getPayload()
                    );
                } else if (event.getEventType() == SystemEventType.EMERGENCY_STOP_ACTIVATED
                        || event.getEventType() == SystemEventType.IRRIGATION_DECISION_MADE
                        || event.getEventType() == SystemEventType.IRRIGATION_EXECUTION_UPDATED) {
                    auditLogService.logIrrigationEvent(
                            event.getEventType().name(),
                            "SYSTEM",
                            "IRRIGATION",
                            event.getFieldId() != null ? event.getFieldId().toString() : "FIELD",
                            event.getAggregateId(),
                            event.getPayload()
                    );
                }
            } catch (Exception e) {
                log.warn("Failed to record audit entry for event {}: {}", event.getEventId(), e.getMessage());
            }
        }
    }
}
