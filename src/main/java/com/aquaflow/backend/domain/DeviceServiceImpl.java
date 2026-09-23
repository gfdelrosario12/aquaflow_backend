package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.DeviceRequest;
import com.aquaflow.backend.dto.response.DeviceResponse;
import com.aquaflow.backend.entity.Device;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.aquaflow.backend.persistence.DeviceRepository;
import com.aquaflow.backend.domain.DeviceService;
import com.aquaflow.backend.infrastructure.util.DtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public DeviceResponse registerDevice(DeviceRequest request) {
        log.info("Registering device: {}", request.getDeviceId());

        if (deviceRepository.existsById(request.getDeviceId())) {
            throw new ValidationException("Device with ID '" + request.getDeviceId() + "' already exists", "DEVICE_DUPLICATE");
        }

        Device device = Device.builder()
                .deviceId(request.getDeviceId())
                .hardwareModel(request.getHardwareModel())
                .firmwareVersion(request.getFirmwareVersion())
                .status("ONLINE")
                .lastHeartbeat(LocalDateTime.now())
                .associatedZones(request.getAssociatedZones())
                .build();

        Device saved = deviceRepository.save(device);
        log.info("Device registered: {}", saved.getDeviceId());
        return DtoMapper.toDeviceResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceResponse getDeviceById(String deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));
        return DtoMapper.toDeviceResponse(device);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponse> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(DtoMapper::toDeviceResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceResponse updateDevice(String deviceId, DeviceRequest request) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));

        device.setHardwareModel(request.getHardwareModel());
        device.setFirmwareVersion(request.getFirmwareVersion());
        device.setAssociatedZones(request.getAssociatedZones());

        Device saved = deviceRepository.save(device);
        log.info("Device updated: {}", deviceId);
        return DtoMapper.toDeviceResponse(saved);
    }

    @Override
    public void deleteDevice(String deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Device not found: " + deviceId);
        }
        deviceRepository.deleteById(deviceId);
        log.info("Device deleted: {}", deviceId);
    }

    @Override
    public DeviceResponse updateHeartbeat(String deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));

        device.setLastHeartbeat(LocalDateTime.now());
        device.setStatus("ONLINE");

        Device saved = deviceRepository.save(device);
        log.info("Heartbeat updated for device: {}", deviceId);
        return DtoMapper.toDeviceResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponse> getDevicesByStatus(String status) {
        return deviceRepository.findByStatus(status).stream()
                .map(DtoMapper::toDeviceResponse)
                .collect(Collectors.toList());
    }
}