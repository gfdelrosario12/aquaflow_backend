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
public class FieldResponse {
    private Long id;
    private String name;
    private String boundaryGeoJson;
    private Double areaHectares;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

