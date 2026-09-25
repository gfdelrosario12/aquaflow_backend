package com.aquaflow.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IrrigationScheduleRequest {

    @NotNull
    private Long zoneId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    @Positive
    private Integer duration;

    @NotNull
    @Positive
    private Double waterVolume;

    private String recurrenceRule;
}