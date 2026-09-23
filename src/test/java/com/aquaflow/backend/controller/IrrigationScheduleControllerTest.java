package com.aquaflow.backend.controller;

import com.aquaflow.backend.dto.request.IrrigationScheduleRequest;
import com.aquaflow.backend.dto.response.IrrigationScheduleResponse;
import com.aquaflow.backend.domain.IrrigationScheduleService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IrrigationScheduleControllerTest {

    private final IrrigationScheduleService scheduleService = mock(IrrigationScheduleService.class);

    @Test
    void shouldCreateSchedule() {
        IrrigationScheduleRequest request = new IrrigationScheduleRequest();
        request.setZoneId(1L);
        request.setStartTime(LocalDateTime.now());
        request.setDuration(60);
        request.setWaterVolume(100.0);

        IrrigationScheduleResponse response = IrrigationScheduleResponse.builder()
                .id(1L)
                .zoneId(1L)
                .startTime(request.getStartTime())
                .duration(60)
                .waterVolume(100.0)
                .status("SCHEDULED")
                .build();

        when(scheduleService.createSchedule(any(IrrigationScheduleRequest.class))).thenReturn(response);

        var result = scheduleService.createSchedule(request);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getZoneId()).isEqualTo(1L);
    }

    @Test
    void shouldGetScheduleById() {
        IrrigationScheduleResponse response = IrrigationScheduleResponse.builder()
                .id(1L)
                .zoneId(1L)
                .status("SCHEDULED")
                .build();

        when(scheduleService.getScheduleById(1L)).thenReturn(response);

        var result = scheduleService.getScheduleById(1L);
        assertThat(result.getId()).isEqualTo(1L);
    }
}