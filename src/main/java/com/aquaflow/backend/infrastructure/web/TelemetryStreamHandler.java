package com.aquaflow.backend.infrastructure.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class TelemetryStreamHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(TelemetryStreamHandler.class);

    private final RealtimeEventPublisherService realtimeEventPublisherService;

    public TelemetryStreamHandler(RealtimeEventPublisherService realtimeEventPublisherService) {
        this.realtimeEventPublisherService = realtimeEventPublisherService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("New WebSocket stream connection established: {}", session.getId());
        realtimeEventPublisherService.registerSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket stream connection closed: {} (Status: {})", session.getId(), status);
        realtimeEventPublisherService.unregisterSession(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.warn("WebSocket transport error for session {}: {}", session.getId(), exception.getMessage());
        realtimeEventPublisherService.unregisterSession(session);
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }
}

