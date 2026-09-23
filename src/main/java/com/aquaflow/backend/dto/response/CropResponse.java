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
public class CropResponse {

    private Long id;
    private String name;
    private Double waterPerStage;
    private Integer growingSeasonDays;
    private Double optimalTemperatureMin;
    private Double optimalTemperatureMax;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}