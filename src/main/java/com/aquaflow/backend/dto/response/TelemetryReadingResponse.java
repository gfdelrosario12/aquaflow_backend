package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.SensorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryReadingResponse {
    private Long id;
    private Long monitoringPointId;
    private Long edgeNodeId;
    private SensorType sensorType;
    private Double valueNum;
    private String unit;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;
}

