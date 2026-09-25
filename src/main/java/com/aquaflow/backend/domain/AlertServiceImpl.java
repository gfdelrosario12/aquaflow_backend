package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.response.AlertResponse;
import com.aquaflow.backend.entity.Alert;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.persistence.AlertRepository;
import com.aquaflow.backend.infrastructure.util.DtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlertServiceImpl implements AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    private final AlertRepository alertRepository;

    public AlertServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public AlertResponse createAlert(String deviceId, Long zoneId, String alertType, String alertLevel, String message) {
        log.info("Creating alert for device: {}, type: {}", deviceId, alertType);

        Alert alert = Alert.builder()
                .deviceId(deviceId)
                .zoneId(zoneId)
                .alertType(alertType)
                .alertLevel(alertLevel)
                .message(message)
                .acknowledged(false)
                .createdAt(LocalDateTime.now())
                .build();

        Alert saved = alertRepository.save(alert);
        log.info("Alert created with id: {}", saved.getId());
        return DtoMapper.toAlertResponse(saved);
    }

    @Override
    public AlertResponse acknowledgeAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + alertId));

        alert.setAcknowledged(true);
        alert.setAcknowledgedAt(LocalDateTime.now());

        Alert saved = alertRepository.save(alert);
        log.info("Alert acknowledged with id: {}", alertId);
        return DtoMapper.toAlertResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getAlertsByZoneId(Long zoneId, Pageable pageable) {
        return alertRepository.findByZoneId(zoneId, pageable).map(DtoMapper::toAlertResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getAlertsByDeviceId(String deviceId, Pageable pageable) {
        return alertRepository.findByDeviceId(deviceId, pageable).map(DtoMapper::toAlertResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertResponse> getUnacknowledgedAlerts() {
        return alertRepository.findByAcknowledgedFalse().stream()
                .map(DtoMapper::toAlertResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertResponse> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(DtoMapper::toAlertResponse)
                .collect(Collectors.toList());
    }
}