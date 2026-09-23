package com.aquaflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ZoneRequest {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Double area;

    @NotBlank
    private String cropType;

    @Positive
    private Double waterAllocationLimit;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }
    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }
    public Double getWaterAllocationLimit() { return waterAllocationLimit; }
    public void setWaterAllocationLimit(Double waterAllocationLimit) { this.waterAllocationLimit = waterAllocationLimit; }
}