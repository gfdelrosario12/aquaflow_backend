package com.aquaflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CropRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double waterPerStage;

    private Integer growingSeasonDays;

    private Double optimalTemperatureMin;

    private Double optimalTemperatureMax;
}