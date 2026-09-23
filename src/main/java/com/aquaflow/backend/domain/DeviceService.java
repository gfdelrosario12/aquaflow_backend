package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.DeviceRequest;
import com.aquaflow.backend.dto.response.DeviceResponse;
import java.util.List;

public interface DeviceService {
    DeviceResponse registerDevice(DeviceRequest request);
    DeviceResponse getDeviceById(String deviceId);
    List<DeviceResponse> getAllDevices();
    DeviceResponse updateDevice(String deviceId, DeviceRequest request);
    void deleteDevice(String deviceId);
    DeviceResponse updateHeartbeat(String deviceId);
    List<DeviceResponse> getDevicesByStatus(String status);
}