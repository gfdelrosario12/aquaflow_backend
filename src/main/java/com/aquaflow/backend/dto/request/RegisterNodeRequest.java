package com.aquaflow.backend.dto.request;

import com.aquaflow.backend.entity.CommunicationIdentityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterNodeRequest {

    @NotBlank(message = "Node ID is required")
    private String nodeId;

    @NotNull(message = "Communication identity type is required")
    private CommunicationIdentityType identityType;

    @NotBlank(message = "Communication identity value is required")
    private String identityValue;

    private String macAddress;
    private String serialNumber;
    private String hardwareModel;
    private String firmwareVersion;
    private Long monitoringZoneId;

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public CommunicationIdentityType getIdentityType() { return identityType; }
    public void setIdentityType(CommunicationIdentityType identityType) { this.identityType = identityType; }
    public String getIdentityValue() { return identityValue; }
    public void setIdentityValue(String identityValue) { this.identityValue = identityValue; }
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public String getHardwareModel() { return hardwareModel; }
    public void setHardwareModel(String hardwareModel) { this.hardwareModel = hardwareModel; }
    public String getFirmwareVersion() { return firmwareVersion; }
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    public Long getMonitoringZoneId() { return monitoringZoneId; }
    public void setMonitoringZoneId(Long monitoringZoneId) { this.monitoringZoneId = monitoringZoneId; }
}

