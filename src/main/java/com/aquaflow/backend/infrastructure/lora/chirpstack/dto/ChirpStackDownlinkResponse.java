package com.aquaflow.backend.infrastructure.lora.chirpstack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChirpStackDownlinkResponse {

    private String id;
    private Long fCnt;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getFCnt() { return fCnt; }
    public void setFCnt(Long fCnt) { this.fCnt = fCnt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

