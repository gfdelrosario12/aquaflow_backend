package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.TelemetryFreshnessChecker;
import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.entity.NodeHealthMetrics;
import com.aquaflow.backend.infrastructure.event.SystemEventPublisher;
import com.aquaflow.backend.persistence.EdgeNodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TelemetryFreshnessCheckerTest {

    @Mock
    private EdgeNodeRepository edgeNodeRepository;

    @Mock
    private SystemEventPublisher systemEventPublisher;

    private TelemetryFreshnessChecker freshnessChecker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        freshnessChecker = new TelemetryFreshnessChecker(edgeNodeRepository, systemEventPublisher);
    }

    @Test
    void shouldDetectStaleTelemetryAndPublishAlarm() {
        NodeHealthMetrics metrics = NodeHealthMetrics.builder()
                .isTelemetryStale(false)
                .lastTelemetryAt(LocalDateTime.now().minusMinutes(45)) // > 30 mins
                .build();

        EdgeNode node = EdgeNode.builder()
                .id(1L)
                .nodeId("NODE-01")
                .healthMetrics(metrics)
                .build();

        when(edgeNodeRepository.findAll()).thenReturn(List.of(node));

        freshnessChecker.checkTelemetryFreshness();

        assertTrue(node.getHealthMetrics().getIsTelemetryStale());
        verify(edgeNodeRepository, times(1)).save(node);
        verify(systemEventPublisher, times(1)).publishAlarm(eq("NODE-01"), any(), eq("STALE_TELEMETRY"), eq("WARNING"), anyString());
    }

    @Test
    void shouldClearStaleTelemetryFlagWhenFresh() {
        NodeHealthMetrics metrics = NodeHealthMetrics.builder()
                .isTelemetryStale(true)
                .lastTelemetryAt(LocalDateTime.now().minusMinutes(5)) // Fresh!
                .build();

        EdgeNode node = EdgeNode.builder()
                .id(1L)
                .nodeId("NODE-01")
                .healthMetrics(metrics)
                .build();

        when(edgeNodeRepository.findAll()).thenReturn(List.of(node));

        freshnessChecker.checkTelemetryFreshness();

        assertFalse(node.getHealthMetrics().getIsTelemetryStale());
        verify(edgeNodeRepository, times(1)).save(node);
    }
}

