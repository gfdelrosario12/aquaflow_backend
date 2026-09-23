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
public class IrrigationScheduleResponse {

    private Long id;
    private Long zoneId;
    private LocalDateTime startTime;
    private Integer duration;
    private Double waterVolume;
    private String recurrenceRule;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}