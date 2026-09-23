package com.aquaflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponse {

    private String deviceId;
    private String hardwareModel;
    private String firmwareVersion;
    private String targetFirmwareVersion;
    private String status;
    private LocalDateTime lastHeartbeat;
    private String associatedZones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}