package com.aquaflow.backend.infrastructure.lora.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoraDownlinkResult {

    private boolean success;
    private String devEui;
    private String downlinkId;
    private String correlationId;
    private String message;
    private Long fCnt;
    private LocalDateTime timestamp;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getDevEui() { return devEui; }
    public void setDevEui(String devEui) { this.devEui = devEui; }
    public String getDownlinkId() { return downlinkId; }
    public void setDownlinkId(String downlinkId) { this.downlinkId = downlinkId; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getFCnt() { return fCnt; }
    public void setFCnt(Long fCnt) { this.fCnt = fCnt; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}

