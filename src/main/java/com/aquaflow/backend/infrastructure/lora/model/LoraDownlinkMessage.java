package com.aquaflow.backend.infrastructure.lora.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoraDownlinkMessage {

    private String devEui;
    private String nodeId;
    private Integer fPort;
    private String payloadBase64;
    private String payloadHex;
    private DownlinkConfirmationMode confirmationMode;
    private String correlationId;

    public String getDevEui() { return devEui; }
    public void setDevEui(String devEui) { this.devEui = devEui; }
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public Integer getFPort() { return fPort; }
    public void setFPort(Integer fPort) { this.fPort = fPort; }
    public String getPayloadBase64() { return payloadBase64; }
    public void setPayloadBase64(String payloadBase64) { this.payloadBase64 = payloadBase64; }
    public String getPayloadHex() { return payloadHex; }
    public void setPayloadHex(String payloadHex) { this.payloadHex = payloadHex; }
    public DownlinkConfirmationMode getConfirmationMode() { return confirmationMode; }
    public void setConfirmationMode(DownlinkConfirmationMode confirmationMode) { this.confirmationMode = confirmationMode; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
}

