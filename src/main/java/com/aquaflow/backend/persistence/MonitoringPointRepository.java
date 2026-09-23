package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.MonitoringPoint;
import com.aquaflow.backend.entity.SensorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoringPointRepository extends JpaRepository<MonitoringPoint, Long> {
    List<MonitoringPoint> findByMonitoringZoneId(Long monitoringZoneId);
    Page<MonitoringPoint> findByMonitoringZoneId(Long monitoringZoneId, Pageable pageable);
    List<MonitoringPoint> findByEdgeNodeId(Long edgeNodeId);
    List<MonitoringPoint> findByPrimarySensorType(SensorType sensorType);
}

