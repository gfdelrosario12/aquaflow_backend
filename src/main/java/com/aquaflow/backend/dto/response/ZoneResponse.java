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
public class ZoneResponse {

    private Long id;
    private String name;
    private Double area;
    private String cropType;
    private Double waterAllocationLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}