package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.NodeHealthMonitor;
import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.entity.HealthState;
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

class NodeHealthMonitorTest {

    @Mock
    private EdgeNodeRepository edgeNodeRepository;

    @Mock
    private SystemEventPublisher systemEventPublisher;

    private NodeHealthMonitor nodeHealthMonitor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nodeHealthMonitor = new NodeHealthMonitor(edgeNodeRepository, systemEventPublisher);
    }

    @Test
    void shouldDetermineHealthyState() {
        NodeHealthMetrics metrics = NodeHealthMetrics.builder()
                .batteryLevel(85.0)
                .signalDbm(-75)
                .consecutiveFailures(0)
                .lastHeartbeat(LocalDateTime.now())
                .build();

        EdgeNode node = EdgeNode.builder()
                .nodeId("NODE-1")
                .healthState(HealthState.HEALTHY)
                .healthMetrics(metrics)
                .updatedAt(LocalDateTime.now())
                .build();

        HealthState computed = nodeHealthMonitor.determineHealthState(node, LocalDateTime.now());
        assertEquals(HealthState.HEALTHY, computed);
    }

    @Test
    void shouldDetermineDegradedStateOnLowBattery() {
        NodeHealthMetrics metrics = NodeHealthMetrics.builder()
                .batteryLevel(15.0) // < 20%
                .signalDbm(-75)
                .lastHeartbeat(LocalDateTime.now())
                .build();

        EdgeNode node = EdgeNode.builder()
                .nodeId("NODE-1")
                .healthState(HealthState.HEALTHY)
                .healthMetrics(metrics)
                .build();

        HealthState computed = nodeHealthMonitor.determineHealthState(node, LocalDateTime.now());
        assertEquals(HealthState.DEGRADED, computed);
    }

    @Test
    void shouldDetermineCriticalStateOnConsecutiveFailures() {
        NodeHealthMetrics metrics = NodeHealthMetrics.builder()
                .batteryLevel(50.0)
                .consecutiveFailures(4) // >= 3
                .lastHeartbeat(LocalDateTime.now())
                .build();

        EdgeNode node = EdgeNode.builder()
                .nodeId("NODE-1")
                .healthState(HealthState.HEALTHY)
                .healthMetrics(metrics)
                .build();

        HealthState computed = nodeHealthMonitor.determineHealthState(node, LocalDateTime.now());
        assertEquals(HealthState.CRITICAL, computed);
    }

    @Test
    void shouldDetermineOfflineStateWhenIncommunicado() {
        NodeHealthMetrics metrics = NodeHealthMetrics.builder()
                .batteryLevel(100.0)
                .lastHeartbeat(LocalDateTime.now().minusMinutes(70)) // > 60 mins
                .build();

        EdgeNode node = EdgeNode.builder()
                .nodeId("NODE-1")
                .healthState(HealthState.HEALTHY)
                .healthMetrics(metrics)
                .build();

        HealthState computed = nodeHealthMonitor.determineHealthState(node, LocalDateTime.now());
        assertEquals(HealthState.OFFLINE, computed);
    }

    @Test
    void shouldEvaluateNodeHealthAndPublishEventsOnTransition() {
        NodeHealthMetrics metrics = NodeHealthMetrics.builder()
                .batteryLevel(5.0) // CRITICAL
                .lastHeartbeat(LocalDateTime.now())
                .build();

        EdgeNode node = EdgeNode.builder()
                .id(1L)
                .nodeId("NODE-01")
                .healthState(HealthState.HEALTHY)
                .healthMetrics(metrics)
                .updatedAt(LocalDateTime.now())
                .build();

        when(edgeNodeRepository.findAll()).thenReturn(List.of(node));

        nodeHealthMonitor.evaluateNodeHealth();

        assertEquals(HealthState.CRITICAL, node.getHealthState());
        verify(edgeNodeRepository, times(1)).save(node);
        verify(systemEventPublisher, times(1)).publishNodeStatusChanged(eq("NODE-01"), any(), eq("HEALTHY"), eq("CRITICAL"));
        verify(systemEventPublisher, times(1)).publishAlarm(eq("NODE-01"), any(), eq("NODE_HEALTH_CRITICAL"), eq("HIGH"), anyString());
    }
}

