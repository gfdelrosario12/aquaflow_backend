package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.response.AlertResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlertService {
    AlertResponse createAlert(String deviceId, Long zoneId, String alertType, String alertLevel, String message);
    AlertResponse acknowledgeAlert(Long alertId);
    Page<AlertResponse> getAlertsByZoneId(Long zoneId, Pageable pageable);
    Page<AlertResponse> getAlertsByDeviceId(String deviceId, Pageable pageable);
    List<AlertResponse> getUnacknowledgedAlerts();
    List<AlertResponse> getAllAlerts();
}