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
public class DownlinkQueueItemResponse {
    private Long id;
    private Long edgeNodeId;
    private String correlationId;
    private String payloadJson;
    private ConfigSyncStatus status;
    private Integer retryCount;
    private LocalDateTime lastAttemptAt;
    private LocalDateTime createdAt;
}

