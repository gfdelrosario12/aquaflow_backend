package com.aquaflow.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplaceNodeRequest {

    @NotNull(message = "Replacement node ID is required")
    private Long replacementNodeId;

    private String replacementReason;
    private Boolean transferMonitoringPoints;

    public Long getReplacementNodeId() { return replacementNodeId; }
    public void setReplacementNodeId(Long replacementNodeId) { this.replacementNodeId = replacementNodeId; }
    public String getReplacementReason() { return replacementReason; }
    public void setReplacementReason(String replacementReason) { this.replacementReason = replacementReason; }
    public Boolean getTransferMonitoringPoints() { return transferMonitoringPoints; }
    public void setTransferMonitoringPoints(Boolean transferMonitoringPoints) { this.transferMonitoringPoints = transferMonitoringPoints; }
}

