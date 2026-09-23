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
public class TelemetryUplinkResponse {

    private boolean success;
    private String nodeId;
    private String message;
    private boolean duplicate;
    private int readingsIngested;
    private LocalDateTime timestamp;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isDuplicate() { return duplicate; }
    public void setDuplicate(boolean duplicate) { this.duplicate = duplicate; }
    public int getReadingsIngested() { return readingsIngested; }
    public void setReadingsIngested(int readingsIngested) { this.readingsIngested = readingsIngested; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}

