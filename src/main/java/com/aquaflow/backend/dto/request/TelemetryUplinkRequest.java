package com.aquaflow.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryUplinkRequest {

    private String nodeId;
    private String devEui;
    private Long fCnt;
    private Integer fPort;
    private String format; // "CHIRPSTACK", "TTN", "JSON"
    private Object payload; // Raw JSON or base64/hex payload string
    private LocalDateTime timestamp;
    private Double batteryLevel;
    private Double solarVoltage;
    private Integer rssiDbm;
    private Double snrDb;
    private String gatewayId;
    private Double frequency;
    private Map<String, Double> measurements;

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public String getDevEui() { return devEui; }
    public void setDevEui(String devEui) { this.devEui = devEui; }
    public Long getFCnt() { return fCnt; }
    public void setFCnt(Long fCnt) { this.fCnt = fCnt; }
    public Integer getFPort() { return fPort; }
    public void setFPort(Integer fPort) { this.fPort = fPort; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public Object getPayload() { return payload; }
    public void setPayload(Object payload) { this.payload = payload; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Double getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Double batteryLevel) { this.batteryLevel = batteryLevel; }
    public Double getSolarVoltage() { return solarVoltage; }
    public void setSolarVoltage(Double solarVoltage) { this.solarVoltage = solarVoltage; }
    public Integer getRssiDbm() { return rssiDbm; }
    public void setRssiDbm(Integer rssiDbm) { this.rssiDbm = rssiDbm; }
    public Double getSnrDb() { return snrDb; }
    public void setSnrDb(Double snrDb) { this.snrDb = snrDb; }
    public String getGatewayId() { return gatewayId; }
    public void setGatewayId(String gatewayId) { this.gatewayId = gatewayId; }
    public Double getFrequency() { return frequency; }
    public void setFrequency(Double frequency) { this.frequency = frequency; }
    public Map<String, Double> getMeasurements() { return measurements; }
    public void setMeasurements(Map<String, Double> measurements) { this.measurements = measurements; }
}

