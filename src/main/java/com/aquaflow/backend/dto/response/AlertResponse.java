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
public class AlertResponse {

    private Long id;
    private String deviceId;
    private Long zoneId;
    private String alertType;
    private String alertLevel;
    private String message;
    private boolean acknowledged;
    private LocalDateTime createdAt;
    private LocalDateTime acknowledgedAt;
}