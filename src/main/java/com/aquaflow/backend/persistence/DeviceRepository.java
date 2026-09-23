package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    List<Device> findByStatus(String status);
    Optional<Device> findByDeviceId(String deviceId);
}