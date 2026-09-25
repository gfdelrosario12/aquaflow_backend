package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, String> {
    List<Device> findByStatus(String status);
    Optional<Device> findByDeviceId(String deviceId);
}