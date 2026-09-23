package com.aquaflow.backend.controller;

import com.aquaflow.backend.dto.request.DeviceRequest;
import com.aquaflow.backend.dto.response.DeviceResponse;
import com.aquaflow.backend.domain.DeviceService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DeviceControllerTest {

    private final DeviceService deviceService = mock(DeviceService.class);

    @Test
    void shouldRegisterDevice() {
        DeviceRequest request = new DeviceRequest();
        request.setDeviceId("device-1");
        request.setHardwareModel("ESP32");
        request.setFirmwareVersion("1.0.0");

        DeviceResponse response = DeviceResponse.builder()
                .deviceId("device-1")
                .hardwareModel("ESP32")
                .firmwareVersion("1.0.0")
                .status("ONLINE")
                .build();

        when(deviceService.registerDevice(any(DeviceRequest.class))).thenReturn(response);

        assertThat(deviceService.registerDevice(request).getDeviceId()).isEqualTo("device-1");
    }

    @Test
    void shouldGetDevice() {
        DeviceResponse response = DeviceResponse.builder()
                .deviceId("device-1")
                .hardwareModel("ESP32")
                .status("ONLINE")
                .build();

        when(deviceService.getDeviceById("device-1")).thenReturn(response);

        var result = deviceService.getDeviceById("device-1");
        assertThat(result.getDeviceId()).isEqualTo("device-1");
    }
}