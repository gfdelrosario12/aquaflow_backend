package com.aquaflow.backend.infrastructure.event;

import com.aquaflow.backend.infrastructure.web.RealtimeEventPublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SystemEventListener {

    private static final Logger log = LoggerFactory.getLogger(SystemEventListener.class);

    private final RealtimeEventPublisherService realtimeEventPublisherService;

    public SystemEventListener(RealtimeEventPublisherService realtimeEventPublisherService) {
        this.realtimeEventPublisherService = realtimeEventPublisherService;
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
    }
}

