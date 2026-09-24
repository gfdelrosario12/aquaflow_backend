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
public class AuditExportResponse {
    private String format; // "csv" or "json"
    private int recordCount;
    private String content; // JSON string or CSV payload
    private LocalDateTime exportedAt;
}

