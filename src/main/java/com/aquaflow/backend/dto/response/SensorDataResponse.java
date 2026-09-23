package com.aquaflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataResponse {

    private Long id;
    private String deviceId;
    private String sensorType;
    private Double value;
    private String unit;
    private LocalDateTime timestamp;
    private Long zoneId;
    private LocalDateTime createdAt;
}