package com.aquaflow.backend.infrastructure.lora.chirpstack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChirpStackUplinkEvent {

    private String deduplicationId;
    private String time;
    private String devEui;
    private Map<String, Object> deviceInfo;
    private Long fCnt;
    private Integer fPort;
    private String data;
    private Map<String, Object> object;

    public String getDeduplicationId() { return deduplicationId; }
    public void setDeduplicationId(String deduplicationId) { this.deduplicationId = deduplicationId; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getDevEui() { return devEui; }
    public void setDevEui(String devEui) { this.devEui = devEui; }
    public Map<String, Object> getDeviceInfo() { return deviceInfo; }
    public void setDeviceInfo(Map<String, Object> deviceInfo) { this.deviceInfo = deviceInfo; }
    public Long getFCnt() { return fCnt; }
    public void setFCnt(Long fCnt) { this.fCnt = fCnt; }
    public Integer getFPort() { return fPort; }
    public void setFPort(Integer fPort) { this.fPort = fPort; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public Map<String, Object> getObject() { return object; }
    public void setObject(Map<String, Object> object) { this.object = object; }
}

