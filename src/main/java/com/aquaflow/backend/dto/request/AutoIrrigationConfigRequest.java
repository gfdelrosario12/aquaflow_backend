package com.aquaflow.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoIrrigationConfigRequest {
    private Boolean enabled;
    private Integer maxDurationMinutes;
    private Integer minCooldownMinutes;
    private Integer allowedStartHour;
    private Integer allowedEndHour;
    private Double targetFloodDepthCm;
    private Integer rainDelayHours;
    private Double minConfidenceThreshold;
    private String updatedBy;
    private String changeReason;

    @Builder.Default
    private List<AwdThresholdConfigRequest> thresholds = new ArrayList<>();

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public Integer getMaxDurationMinutes() { return maxDurationMinutes; }
    public void setMaxDurationMinutes(Integer maxDurationMinutes) { this.maxDurationMinutes = maxDurationMinutes; }
    public Integer getMinCooldownMinutes() { return minCooldownMinutes; }
    public void setMinCooldownMinutes(Integer minCooldownMinutes) { this.minCooldownMinutes = minCooldownMinutes; }
    public Integer getAllowedStartHour() { return allowedStartHour; }
    public void setAllowedStartHour(Integer allowedStartHour) { this.allowedStartHour = allowedStartHour; }
    public Integer getAllowedEndHour() { return allowedEndHour; }
    public void setAllowedEndHour(Integer allowedEndHour) { this.allowedEndHour = allowedEndHour; }
    public Double getTargetFloodDepthCm() { return targetFloodDepthCm; }
    public void setTargetFloodDepthCm(Double targetFloodDepthCm) { this.targetFloodDepthCm = targetFloodDepthCm; }
    public Integer getRainDelayHours() { return rainDelayHours; }
    public void setRainDelayHours(Integer rainDelayHours) { this.rainDelayHours = rainDelayHours; }
    public Double getMinConfidenceThreshold() { return minConfidenceThreshold; }
    public void setMinConfidenceThreshold(Double minConfidenceThreshold) { this.minConfidenceThreshold = minConfidenceThreshold; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
    public List<AwdThresholdConfigRequest> getThresholds() { return thresholds; }
    public void setThresholds(List<AwdThresholdConfigRequest> thresholds) { this.thresholds = thresholds; }
}

