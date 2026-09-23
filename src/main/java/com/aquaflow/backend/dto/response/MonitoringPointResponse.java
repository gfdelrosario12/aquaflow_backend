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
public class MonitoringPointResponse {
    private Long id;
    private String name;
    private Long monitoringZoneId;
    private Long edgeNodeId;
    private SensorType primarySensorType;
    private Double depthCm;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

