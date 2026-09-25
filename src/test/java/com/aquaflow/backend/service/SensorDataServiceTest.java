package com.aquaflow.backend.service;

import com.aquaflow.backend.entity.SensorReading;
import com.aquaflow.backend.entity.SensorType;
import com.aquaflow.backend.persistence.SensorReadingRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SensorDataServiceTest {

    private final SensorReadingRepository sensorReadingRepository = mock(SensorReadingRepository.class);

    @Test
    void shouldIngestSensorData() {
        SensorReading reading = SensorReading.builder()
                .deviceId("device-1")
                .sensorType(SensorType.TEMPERATURE)
                .value(25.5)
                .unit("C")
                .timestamp(LocalDateTime.now())
                .build();

        when(sensorReadingRepository.save(any(SensorReading.class))).thenReturn(reading);

        var saved = sensorReadingRepository.save(reading);
        assertThat(saved.getDeviceId()).isEqualTo("device-1");
    }

    @Test
    void shouldFindReadingsByDeviceId() {
        when(sensorReadingRepository.findByDeviceId("device-1"))
                .thenReturn(List.of(
                        SensorReading.builder().deviceId("device-1").sensorType(SensorType.TEMPERATURE).value(25.0).build(),
                        SensorReading.builder().deviceId("device-1").sensorType(SensorType.HUMIDITY).value(60.0).build()
                ));

        var readings = sensorReadingRepository.findByDeviceId("device-1");
        assertThat(readings).hasSize(2);
    }

    @Test
    void shouldFindReadingById() {
        SensorReading reading = SensorReading.builder()
                .id(1L)
                .deviceId("device-1")
                .sensorType(SensorType.TEMPERATURE)
                .value(25.5)
                .build();

        when(sensorReadingRepository.findById(1L)).thenReturn(Optional.of(reading));

        var result = sensorReadingRepository.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().getDeviceId()).isEqualTo("device-1");
    }
}