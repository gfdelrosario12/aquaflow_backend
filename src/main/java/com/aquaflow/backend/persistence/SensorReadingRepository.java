package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.SensorReading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    List<SensorReading> findByDeviceId(String deviceId);
    Page<SensorReading> findByDeviceId(String deviceId, Pageable pageable);
    List<SensorReading> findByZoneId(Long zoneId);
    Page<SensorReading> findByZoneId(Long zoneId, Pageable pageable);
    List<SensorReading> findByDeviceIdAndTimestampBetween(String deviceId, LocalDateTime from, LocalDateTime to);
    Page<SensorReading> findByDeviceIdAndTimestampBetween(String deviceId, LocalDateTime from, LocalDateTime to, Pageable pageable);
}