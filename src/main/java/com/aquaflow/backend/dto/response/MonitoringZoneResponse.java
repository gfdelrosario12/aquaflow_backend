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
public class MonitoringZoneResponse {
    private Long id;
    private String name;
    private Long fieldId;
    private String cropType;
    private Double targetMoisturePercentage;
    private Double waterAllocationLimitLiters;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

