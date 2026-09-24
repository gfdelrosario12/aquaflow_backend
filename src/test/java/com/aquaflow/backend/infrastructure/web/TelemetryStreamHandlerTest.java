package com.aquaflow.backend.infrastructure.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TelemetryStreamHandlerTest {

    @Mock
    private RealtimeEventPublisherService realtimeEventPublisherService;

    @Mock
    private WebSocketSession session;

    private TelemetryStreamHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new TelemetryStreamHandler(realtimeEventPublisherService);
        when(session.getId()).thenReturn("sess-abc");
    }

    @Test
    void shouldRegisterSessionAfterConnectionEstablished() throws Exception {
        handler.afterConnectionEstablished(session);
        verify(realtimeEventPublisherService).registerSession(session);
    }

    @Test
    void shouldUnregisterSessionAfterConnectionClosed() throws Exception {
        handler.afterConnectionClosed(session, CloseStatus.NORMAL);
        verify(realtimeEventPublisherService).unregisterSession(session);
    }

    @Test
    void shouldUnregisterSessionOnTransportError() throws Exception {
        when(session.isOpen()).thenReturn(false);
        handler.handleTransportError(session, new RuntimeException("Network error"));
        verify(realtimeEventPublisherService).unregisterSession(session);
    }
}

