package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.ConfigSyncStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigSyncStatusResponse {
    private Long edgeNodeId;
    private String nodeIdentifier;
    private Long configVersion;
    private ConfigSyncStatus status;
    private String correlationId;
    private Integer retryCount;
    private String failureReason;
    private LocalDateTime lastSyncedAt;
    private LocalDateTime updatedAt;
}

