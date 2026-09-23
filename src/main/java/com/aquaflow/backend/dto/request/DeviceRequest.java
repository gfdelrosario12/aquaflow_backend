package com.aquaflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceRequest {

    @NotBlank
    private String deviceId;

    private String hardwareModel;

    private String firmwareVersion;

    private String associatedZones;
}