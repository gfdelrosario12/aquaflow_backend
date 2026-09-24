package com.aquaflow.backend.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.aquaflow.backend.infrastructure.event.SystemEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RealtimeEventPublisherServiceImpl implements RealtimeEventPublisherService {

    private static final Logger log = LoggerFactory.getLogger(RealtimeEventPublisherServiceImpl.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public RealtimeEventPublisherServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void registerSession(WebSocketSession session) {
        if (session != null && session.isOpen()) {
            sessions.put(session.getId(), session);
            log.info("WebSocket session registered: {}. Total active sessions: {}", session.getId(), sessions.size());
        }
    }

    @Override
    public void unregisterSession(WebSocketSession session) {
        if (session != null) {
            sessions.remove(session.getId());
            log.info("WebSocket session unregistered: {}. Total active sessions: {}", session.getId(), sessions.size());
        }
    }

    @Override
    public void broadcastEvent(SystemEvent event) {
        if (event == null || sessions.isEmpty()) {
            return;
        }

        String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            log.error("Failed to serialize SystemEvent {}: {}", event.getEventId(), e.getMessage());
            return;
        }

        TextMessage message = new TextMessage(jsonPayload);

        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            WebSocketSession session = entry.getValue();
            if (session.isOpen()) {
                try {
                    synchronized (session) {
                        session.sendMessage(message);
                    }
                } catch (IOException e) {
                    log.warn("Failed to send WebSocket event to session {}: {}", session.getId(), e.getMessage());
                    unregisterSession(session);
                } catch (Exception e) {
                    log.error("Unexpected error delivering WebSocket event to session {}: {}", session.getId(), e.getMessage());
                }
            } else {
                unregisterSession(session);
            }
        }
    }

    @Override
    public int getActiveSessionCount() {
        return sessions.size();
    }
}

