package com.aquaflow.backend.dto.response;

import com.aquaflow.backend.entity.NodeLifecycleState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommissionNodeResponse {

    private Long id;
    private String nodeId;
    private NodeLifecycleState lifecycleState;
    private LocalDateTime commissionedAt;
    private String singleUseCommissioningToken;
    private LocalDateTime tokenExpiresAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public NodeLifecycleState getLifecycleState() { return lifecycleState; }
    public void setLifecycleState(NodeLifecycleState lifecycleState) { this.lifecycleState = lifecycleState; }
    public LocalDateTime getCommissionedAt() { return commissionedAt; }
    public void setCommissionedAt(LocalDateTime commissionedAt) { this.commissionedAt = commissionedAt; }
    public String getSingleUseCommissioningToken() { return singleUseCommissioningToken; }
    public void setSingleUseCommissioningToken(String singleUseCommissioningToken) { this.singleUseCommissioningToken = singleUseCommissioningToken; }
    public LocalDateTime getTokenExpiresAt() { return tokenExpiresAt; }
    public void setTokenExpiresAt(LocalDateTime tokenExpiresAt) { this.tokenExpiresAt = tokenExpiresAt; }
}

