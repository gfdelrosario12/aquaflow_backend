package com.aquaflow.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignalMetadataDto {

    private Integer rssiDbm;
    private Double snrDb;
    private String gatewayId;
    private Double frequency;

    public Integer getRssiDbm() { return rssiDbm; }
    public void setRssiDbm(Integer rssiDbm) { this.rssiDbm = rssiDbm; }
    public Double getSnrDb() { return snrDb; }
    public void setSnrDb(Double snrDb) { this.snrDb = snrDb; }
    public String getGatewayId() { return gatewayId; }
    public void setGatewayId(String gatewayId) { this.gatewayId = gatewayId; }
    public Double getFrequency() { return frequency; }
    public void setFrequency(Double frequency) { this.frequency = frequency; }
}

