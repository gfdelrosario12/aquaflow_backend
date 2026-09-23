package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.SensorDataRequest;
import com.aquaflow.backend.dto.response.SensorDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorDataService {
    SensorDataResponse ingestSensorData(SensorDataRequest request);
    SensorDataResponse getReadingById(Long id);
    Page<SensorDataResponse> getReadingsByDeviceId(String deviceId, Pageable pageable);
    Page<SensorDataResponse> getReadingsByZoneId(Long zoneId, Pageable pageable);
    Page<SensorDataResponse> getReadingsByDeviceIdAndTimeRange(String deviceId, LocalDateTime from, LocalDateTime to, Pageable pageable);
    List<SensorDataResponse> getReadingsByDeviceId(String deviceId);
}