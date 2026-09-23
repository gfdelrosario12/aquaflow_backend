package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.IrrigationScheduleRequest;
import com.aquaflow.backend.dto.response.IrrigationScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IrrigationScheduleService {
    IrrigationScheduleResponse createSchedule(IrrigationScheduleRequest request);
    IrrigationScheduleResponse getScheduleById(Long id);
    Page<IrrigationScheduleResponse> getAllSchedules(Pageable pageable);
    IrrigationScheduleResponse updateSchedule(Long id, IrrigationScheduleRequest request);
    void deleteSchedule(Long id);
    List<IrrigationScheduleResponse> getSchedulesByZoneId(Long zoneId);
    List<IrrigationScheduleResponse> getSchedulesByZoneIdOrderByStartTimeAsc(Long zoneId);
}