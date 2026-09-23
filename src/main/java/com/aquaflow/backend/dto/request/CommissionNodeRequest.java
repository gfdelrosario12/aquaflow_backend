package com.aquaflow.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommissionNodeRequest {

    private Long monitoringZoneId;
    private String commissioningNotes;
    private Integer txPowerDbm;
    private Integer intervalSeconds;

    public Long getMonitoringZoneId() { return monitoringZoneId; }
    public void setMonitoringZoneId(Long monitoringZoneId) { this.monitoringZoneId = monitoringZoneId; }
    public String getCommissioningNotes() { return commissioningNotes; }
    public void setCommissioningNotes(String commissioningNotes) { this.commissioningNotes = commissioningNotes; }
    public Integer getTxPowerDbm() { return txPowerDbm; }
    public void setTxPowerDbm(Integer txPowerDbm) { this.txPowerDbm = txPowerDbm; }
    public Integer getIntervalSeconds() { return intervalSeconds; }
    public void setIntervalSeconds(Integer intervalSeconds) { this.intervalSeconds = intervalSeconds; }
}

