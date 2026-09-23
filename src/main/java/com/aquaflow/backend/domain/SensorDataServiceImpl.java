package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.SensorDataRequest;
import com.aquaflow.backend.dto.response.SensorDataResponse;
import com.aquaflow.backend.entity.SensorReading;
import com.aquaflow.backend.entity.SensorType;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.aquaflow.backend.persistence.SensorReadingRepository;
import com.aquaflow.backend.domain.SensorDataService;
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
public class SensorDataServiceImpl implements SensorDataService {

    private static final Logger log = LoggerFactory.getLogger(SensorDataServiceImpl.class);

    private final SensorReadingRepository sensorReadingRepository;

    public SensorDataServiceImpl(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    @Override
    public SensorDataResponse ingestSensorData(SensorDataRequest request) {
        log.info("Ingesting sensor data from device: {}", request.getDeviceId());

        if (request.getValue() == null) {
            throw new ValidationException("Sensor value must not be null", "SENSOR_VALUE_NULL");
        }

        SensorReading reading = SensorReading.builder()
                .deviceId(request.getDeviceId())
                .sensorType(request.getSensorType())
                .sensorType(request.getSensorType() != null ? SensorType.valueOf(request.getSensorType()) : SensorType.SOIL_MOISTURE)
                .value(request.getValue())
                .unit(request.getUnit())
                .timestamp(LocalDateTime.now())
                .zoneId(request.getZoneId())
                .build();

        SensorReading saved = sensorReadingRepository.save(reading);
        log.info("Sensor reading saved with id: {}", saved.getId());
        return DtoMapper.toSensorDataResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SensorDataResponse getReadingById(Long id) {
        SensorReading reading = sensorReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor reading not found with id: " + id));
        return DtoMapper.toSensorDataResponse(reading);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorDataResponse> getReadingsByDeviceId(String deviceId, Pageable pageable) {
        return sensorReadingRepository.findByDeviceId(deviceId, pageable).map(DtoMapper::toSensorDataResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorDataResponse> getReadingsByZoneId(Long zoneId, Pageable pageable) {
        return sensorReadingRepository.findByZoneId(zoneId, pageable).map(DtoMapper::toSensorDataResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorDataResponse> getReadingsByDeviceIdAndTimeRange(String deviceId, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return sensorReadingRepository.findByDeviceIdAndTimestampBetween(deviceId, from, to, pageable)
                .map(DtoMapper::toSensorDataResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorDataResponse> getReadingsByDeviceId(String deviceId) {
        return sensorReadingRepository.findByDeviceId(deviceId).stream()
                .map(DtoMapper::toSensorDataResponse)
                .collect(Collectors.toList());
    }
}