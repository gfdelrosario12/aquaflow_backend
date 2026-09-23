package com.aquaflow.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransmissionRequest {

    @NotNull(message = "Transmission interval is required")
    @Positive(message = "Interval must be a positive integer in seconds")
    private Integer intervalSeconds;

    @Min(value = -30, message = "Transmit power must be at least -30 dBm")
    @Max(value = 30, message = "Transmit power must be at most 30 dBm")
    private Integer txPowerDbm;

    @Min(value = 0, message = "Duty cycle must be non-negative")
    @Max(value = 100, message = "Duty cycle cannot exceed 100%")
    private Double dutyCyclePercentage;

    public Integer getIntervalSeconds() { return intervalSeconds; }
    public void setIntervalSeconds(Integer intervalSeconds) { this.intervalSeconds = intervalSeconds; }
    public Integer getTxPowerDbm() { return txPowerDbm; }
    public void setTxPowerDbm(Integer txPowerDbm) { this.txPowerDbm = txPowerDbm; }
    public Double getDutyCyclePercentage() { return dutyCyclePercentage; }
    public void setDutyCyclePercentage(Double dutyCyclePercentage) { this.dutyCyclePercentage = dutyCyclePercentage; }
}

