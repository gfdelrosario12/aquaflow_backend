package com.aquaflow.backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryBatchUplinkRequest {

    @NotNull(message = "Uplinks list must not be null")
    @NotEmpty(message = "Uplinks list must not be empty")
    @Valid
    private List<TelemetryUplinkRequest> uplinks;

    public List<TelemetryUplinkRequest> getUplinks() { return uplinks; }
    public void setUplinks(List<TelemetryUplinkRequest> uplinks) { this.uplinks = uplinks; }
}

