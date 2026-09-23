package com.aquaflow.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationIdentity {

    @Enumerated(EnumType.STRING)
    @Column(name = "identity_type", nullable = false)
    private CommunicationIdentityType identityType;

    @Column(name = "identity_value", nullable = false)
    private String identityValue;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "serial_number")
    private String serialNumber;
}

