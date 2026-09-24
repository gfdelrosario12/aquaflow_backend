package com.aquaflow.backend.infrastructure.web;

import com.aquaflow.backend.infrastructure.event.SystemEvent;
import org.springframework.web.socket.WebSocketSession;

public interface RealtimeEventPublisherService {

    void registerSession(WebSocketSession session);

    void unregisterSession(WebSocketSession session);

    void broadcastEvent(SystemEvent event);

    int getActiveSessionCount();
}

