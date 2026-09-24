package com.aquaflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneTelemetryTrendResponse {

    private Long zoneId;
    private Long fieldId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<ZoneTelemetryTrendPoint> dataPoints;

    public Long getZoneId() { return zoneId; }
    public void setZoneId(Long zoneId) { this.zoneId = zoneId; }
    public Long getFieldId() { return fieldId; }
    public void setFieldId(Long fieldId) { this.fieldId = fieldId; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public List<ZoneTelemetryTrendPoint> getDataPoints() { return dataPoints; }
    public void setDataPoints(List<ZoneTelemetryTrendPoint> dataPoints) { this.dataPoints = dataPoints; }
}

