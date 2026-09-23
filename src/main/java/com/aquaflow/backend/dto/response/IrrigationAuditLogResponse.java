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
public class IrrigationAuditLogResponse {
    private Long id;
    private String eventType;
    private String actor;
    private String entityType;
    private String entityId;
    private String payloadJson;
    private LocalDateTime createdAt;
}

