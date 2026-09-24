package com.aquaflow.backend.dto.request;

import com.aquaflow.backend.entity.CropGrowthStage;
import com.aquaflow.backend.entity.DecisionType;
import com.aquaflow.backend.entity.TriggerReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IrrigationDecisionRequest {
    private Long edgeNodeId;
    private String nodeId;
    private DecisionType decisionType;
    private TriggerReason triggerReason;
    private CropGrowthStage cropStage;
    private Double confidence;
    private Integer requestedDurationMinutes;
    private Double requestedVolumeLiters;
    private Integer actualDurationMinutes;
    private String executionStatus;
    private String failureReason;
    private Long configVersion;
    private String correlationId;
    private Double telemetryWaterLevelCm;
    private Double telemetrySoilMoisturePercent;
    private Double telemetryTemperatureC;
    private Double batteryPercentage;
    private Integer rssi;
    private Double snr;
    private LocalDateTime nodeTimestamp;
}

