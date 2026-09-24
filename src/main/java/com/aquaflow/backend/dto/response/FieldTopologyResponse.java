package com.aquaflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldTopologyResponse {
    private Long id;
    private String name;
    private String boundaryGeoJson;
    private Double areaHectares;
    private Long activeConfigVersion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<MonitoringZoneTopologyResponse> zones = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBoundaryGeoJson() { return boundaryGeoJson; }
    public void setBoundaryGeoJson(String boundaryGeoJson) { this.boundaryGeoJson = boundaryGeoJson; }
    public Double getAreaHectares() { return areaHectares; }
    public void setAreaHectares(Double areaHectares) { this.areaHectares = areaHectares; }
    public Long getActiveConfigVersion() { return activeConfigVersion; }
    public void setActiveConfigVersion(Long activeConfigVersion) { this.activeConfigVersion = activeConfigVersion; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<MonitoringZoneTopologyResponse> getZones() { return zones; }
    public void setZones(List<MonitoringZoneTopologyResponse> zones) { this.zones = zones; }
}
