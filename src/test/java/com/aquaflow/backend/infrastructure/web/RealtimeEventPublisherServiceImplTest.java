package com.aquaflow.backend.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.aquaflow.backend.infrastructure.event.SystemEvent;
import com.aquaflow.backend.infrastructure.event.SystemEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RealtimeEventPublisherServiceImplTest {

    @Mock
    private WebSocketSession session;

    private RealtimeEventPublisherServiceImpl publisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        publisher = new RealtimeEventPublisherServiceImpl(objectMapper);
        when(session.getId()).thenReturn("sess-1");
        when(session.isOpen()).thenReturn(true);
    }

    @Test
    void shouldRegisterAndUnregisterSession() {
        publisher.registerSession(session);
        assertThat(publisher.getActiveSessionCount()).isEqualTo(1);

        publisher.unregisterSession(session);
        assertThat(publisher.getActiveSessionCount()).isEqualTo(0);
    }

    @Test
    void shouldBroadcastEventToOpenSession() throws Exception {
        publisher.registerSession(session);

        SystemEvent event = SystemEvent.builder()
                .eventId("ev-123")
                .eventType(SystemEventType.TELEMETRY_RECEIVED)
                .aggregateId("NODE-01")
                .fieldId(1L)
                .build();

        publisher.broadcastEvent(event);

        verify(session).sendMessage(any(TextMessage.class));
    }

    @Test
    void shouldRemoveSessionWhenIOExceptionOccurs() throws Exception {
        publisher.registerSession(session);

        doThrow(new IOException("Socket closed")).when(session).sendMessage(any(TextMessage.class));

        SystemEvent event = SystemEvent.builder()
                .eventId("ev-456")
                .eventType(SystemEventType.ALARM_TRIGGERED)
                .build();

        publisher.broadcastEvent(event);

        assertThat(publisher.getActiveSessionCount()).isEqualTo(0);
    }
}

