package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByZoneId(Long zoneId);
    Page<Alert> findByZoneId(Long zoneId, Pageable pageable);
    List<Alert> findByDeviceId(String deviceId);
    Page<Alert> findByDeviceId(String deviceId, Pageable pageable);
    List<Alert> findByAcknowledgedFalse();
}