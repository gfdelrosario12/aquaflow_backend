package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.ZoneAggregationServiceImpl;
import com.aquaflow.backend.dto.response.ZoneTelemetryResponse;
import com.aquaflow.backend.dto.response.ZoneTelemetryTrendResponse;
import com.aquaflow.backend.entity.*;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.persistence.EdgeNodeRepository;
import com.aquaflow.backend.persistence.MonitoringZoneRepository;
import com.aquaflow.backend.persistence.TelemetryReadingRepository;
import com.aquaflow.backend.persistence.ZoneTelemetryAggregateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ZoneAggregationServiceImplTest {

    @Mock
    private MonitoringZoneRepository monitoringZoneRepository;

    @Mock
    private EdgeNodeRepository edgeNodeRepository;

    @Mock
    private TelemetryReadingRepository telemetryReadingRepository;

    @Mock
    private ZoneTelemetryAggregateRepository zoneTelemetryAggregateRepository;

    private ZoneAggregationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ZoneAggregationServiceImpl(
                monitoringZoneRepository,
                edgeNodeRepository,
                telemetryReadingRepository,
                zoneTelemetryAggregateRepository
        );
    }

    @Test
    void shouldCalculateZoneTelemetryForActiveOnlineNodes() {
        Field field = Field.builder().id(10L).name("North Field").build();
        MonitoringZone zone = MonitoringZone.builder().id(1L).name("Zone 1").field(field).build();

        NodeHealthMetrics metrics = NodeHealthMetrics.builder()
                .batteryLevel(3.8)
                .signalDbm(-80)
                .lastHeartbeat(LocalDateTime.now())
                .build();

        EdgeNode node1 = EdgeNode.builder()
                .id(101L)
                .nodeId("NODE-01")
                .lifecycleState(NodeLifecycleState.COMMISSIONED)
                .healthState(HealthState.HEALTHY)
                .healthMetrics(metrics)
                .monitoringZone(zone)
                .build();

        TelemetryReading reading1 = TelemetryReading.builder()
                .edgeNode(node1)
                .sensorType(SensorType.SOIL_MOISTURE)
                .valueNum(35.0)
                .timestamp(LocalDateTime.now())
                .build();

        TelemetryReading reading2 = TelemetryReading.builder()
                .edgeNode(node1)
                .sensorType(SensorType.TEMPERATURE)
                .valueNum(28.5)
                .timestamp(LocalDateTime.now())
                .build();

        when(monitoringZoneRepository.findById(1L)).thenReturn(Optional.of(zone));
        when(edgeNodeRepository.findByMonitoringZoneId(1L)).thenReturn(List.of(node1));
        when(telemetryReadingRepository.findByEdgeNodeIdOrderByTimestampDesc(101L)).thenReturn(List.of(reading1, reading2));
        when(zoneTelemetryAggregateRepository.save(any(ZoneTelemetryAggregate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ZoneTelemetryResponse response = service.calculateAndCacheZoneTelemetry(1L);

        assertThat(response).isNotNull();
        assertThat(response.getZoneId()).isEqualTo(1L);
        assertThat(response.getFieldId()).isEqualTo(10L);
        assertThat(response.getAvgSoilMoisture()).isEqualTo(35.0);
        assertThat(response.getAvgTemperature()).isEqualTo(28.5);
        assertThat(response.getOnlineNodes()).isEqualTo(1);
        assertThat(response.getHealthStatus()).isEqualTo(ZoneHealthStatus.OPTIMAL);
    }

    @Test
    void shouldHandleEmptyZoneWithZeroNodes() {
        MonitoringZone zone = MonitoringZone.builder().id(2L).name("Empty Zone").build();

        when(monitoringZoneRepository.findById(2L)).thenReturn(Optional.of(zone));
        when(edgeNodeRepository.findByMonitoringZoneId(2L)).thenReturn(List.of());
        when(zoneTelemetryAggregateRepository.save(any(ZoneTelemetryAggregate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ZoneTelemetryResponse response = service.calculateAndCacheZoneTelemetry(2L);

        assertThat(response).isNotNull();
        assertThat(response.getHealthStatus()).isEqualTo(ZoneHealthStatus.OFFLINE);
        assertThat(response.getTotalNodes()).isEqualTo(0);
    }

    @Test
    void shouldThrowExceptionWhenZoneNotFound() {
        when(monitoringZoneRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.calculateAndCacheZoneTelemetry(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldReturnCachedTelemetryWhenFresh() {
        ZoneTelemetryAggregate cached = ZoneTelemetryAggregate.builder()
                .zoneId(1L)
                .fieldId(10L)
                .avgSoilMoisture(40.0)
                .healthStatus(ZoneHealthStatus.OPTIMAL)
                .calculatedAt(LocalDateTime.now().minusMinutes(2))
                .build();

        when(zoneTelemetryAggregateRepository.findTopByZoneIdOrderByCalculatedAtDesc(1L))
                .thenReturn(Optional.of(cached));

        ZoneTelemetryResponse response = service.getLatestZoneTelemetry(1L);

        assertThat(response).isNotNull();
        assertThat(response.getAvgSoilMoisture()).isEqualTo(40.0);
    }

    @Test
    void shouldFetchZoneTelemetryTrend() {
        LocalDateTime now = LocalDateTime.now();
        ZoneTelemetryAggregate agg = ZoneTelemetryAggregate.builder()
                .zoneId(1L)
                .fieldId(10L)
                .avgSoilMoisture(32.0)
                .avgTemperature(26.0)
                .waterLevel(12.0)
                .healthStatus(ZoneHealthStatus.OPTIMAL)
                .calculatedAt(now.minusHours(1))
                .build();

        when(zoneTelemetryAggregateRepository.findByZoneIdAndCalculatedAtBetweenOrderByCalculatedAtAsc(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(agg));

        ZoneTelemetryTrendResponse trend = service.getZoneTelemetryTrend(10L, 1L, now.minusHours(24), now);

        assertThat(trend).isNotNull();
        assertThat(trend.getZoneId()).isEqualTo(1L);
        assertThat(trend.getDataPoints()).hasSize(1);
        assertThat(trend.getDataPoints().get(0).getAvgSoilMoisture()).isEqualTo(32.0);
    }
}

