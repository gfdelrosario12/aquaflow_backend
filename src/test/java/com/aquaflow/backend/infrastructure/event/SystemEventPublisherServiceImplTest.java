package com.aquaflow.backend.infrastructure.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class SystemEventPublisherServiceImplTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private SystemEventPublisherServiceImpl publisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        publisher = new SystemEventPublisherServiceImpl(applicationEventPublisher);
    }

    @Test
    void shouldPublishTelemetryReceivedEvent() {
        publisher.publishTelemetryReceived("NODE-001", 1L, 3);

        ArgumentCaptor<SystemEvent> captor = ArgumentCaptor.forClass(SystemEvent.class);
        verify(applicationEventPublisher).publishEvent(captor.capture());

        SystemEvent event = captor.getValue();
        assertThat(event).isNotNull();
        assertThat(event.getEventType()).isEqualTo(SystemEventType.TELEMETRY_RECEIVED);
        assertThat(event.getAggregateId()).isEqualTo("NODE-001");
        assertThat(event.getFieldId()).isEqualTo(1L);
    }

    @Test
    void shouldPublishNodeStatusChangedEvent() {
        publisher.publishNodeStatusChanged("NODE-002", 2L, "OFFLINE", "COMMISSIONED");

        ArgumentCaptor<SystemEvent> captor = ArgumentCaptor.forClass(SystemEvent.class);
        verify(applicationEventPublisher).publishEvent(captor.capture());

        SystemEvent event = captor.getValue();
        assertThat(event.getEventType()).isEqualTo(SystemEventType.NODE_STATUS_CHANGED);
        assertThat(event.getAggregateId()).isEqualTo("NODE-002");
    }

    @Test
    void shouldPublishEmergencyStopEvent() {
        publisher.publishEmergencyStop("ZONE-05", 3L, "Manual safety stop");

        ArgumentCaptor<SystemEvent> captor = ArgumentCaptor.forClass(SystemEvent.class);
        verify(applicationEventPublisher).publishEvent(captor.capture());

        SystemEvent event = captor.getValue();
        assertThat(event.getEventType()).isEqualTo(SystemEventType.EMERGENCY_STOP_ACTIVATED);
        assertThat(event.getAggregateId()).isEqualTo("ZONE-05");
    }
}

