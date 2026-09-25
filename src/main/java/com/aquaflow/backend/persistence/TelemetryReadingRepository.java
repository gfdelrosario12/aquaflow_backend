package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.SensorType;
import com.aquaflow.backend.entity.TelemetryReading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TelemetryReadingRepository extends JpaRepository<TelemetryReading, Long> {
    List<TelemetryReading> findByMonitoringPointId(Long monitoringPointId);
    Page<TelemetryReading> findByMonitoringPointId(Long monitoringPointId, Pageable pageable);
    Page<TelemetryReading> findByMonitoringPointIdAndTimestampBetween(Long monitoringPointId, LocalDateTime from, LocalDateTime to, Pageable pageable);
    Page<TelemetryReading> findByEdgeNodeId(Long edgeNodeId, Pageable pageable);
    List<TelemetryReading> findByEdgeNodeIdOrderByTimestampDesc(Long edgeNodeId);
    Page<TelemetryReading> findByEdgeNodeIdAndTimestampBetween(Long edgeNodeId, LocalDateTime from, LocalDateTime to, Pageable pageable);
    List<TelemetryReading> findByMonitoringPointIdAndSensorTypeOrderByTimestampDesc(Long monitoringPointId, SensorType sensorType);
}

