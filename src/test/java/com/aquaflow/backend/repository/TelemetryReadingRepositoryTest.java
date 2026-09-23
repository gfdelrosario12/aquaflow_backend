package com.aquaflow.backend.repository;

import com.aquaflow.backend.entity.SensorType;
import com.aquaflow.backend.entity.TelemetryReading;
import com.aquaflow.backend.persistence.TelemetryReadingRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TelemetryReadingRepositoryTest {

    private final TelemetryReadingRepository telemetryReadingRepository = mock(TelemetryReadingRepository.class);

    @Test
    void shouldSaveAndRetrieveTelemetryReadings() {
        LocalDateTime now = LocalDateTime.now();
        TelemetryReading reading = TelemetryReading.builder()
                .id(100L)
                .sensorType(SensorType.SOIL_MOISTURE)
                .valueNum(42.5)
                .unit("%")
                .timestamp(now)
                .createdAt(now)
                .build();

        when(telemetryReadingRepository.findByMonitoringPointId(1L)).thenReturn(List.of(reading));

        var readings = telemetryReadingRepository.findByMonitoringPointId(1L);
        assertThat(readings).hasSize(1);
        assertThat(readings.get(0).getSensorType()).isEqualTo(SensorType.SOIL_MOISTURE);
        assertThat(readings.get(0).getValueNum()).isEqualTo(42.5);
    }
}

