package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.CommandLifecycleState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandStatusResponse {
    private String correlationId;
    private Long fieldId;
    private String operatorId;
    private CommandLifecycleState status;
    private String commandType;
    private String rationale;
    private String details;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

