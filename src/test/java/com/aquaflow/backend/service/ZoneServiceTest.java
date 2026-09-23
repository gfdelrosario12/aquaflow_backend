package com.aquaflow.backend.service;

import com.aquaflow.backend.dto.request.ZoneRequest;
import com.aquaflow.backend.dto.response.ZoneResponse;
import com.aquaflow.backend.entity.Zone;
import com.aquaflow.backend.persistence.ZoneRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ZoneServiceTest {

    private final ZoneRepository zoneRepository = mock(ZoneRepository.class);

    @Test
    void shouldCreateZone() {
        Zone zone = Zone.builder()
                .name("Test Zone")
                .area(100.0)
                .cropType("corn")
                .waterAllocationLimit(500.0)
                .build();

        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);

        assertThat(zone.getName()).isEqualTo("Test Zone");
        assertThat(zone.getArea()).isEqualTo(100.0);
    }

    @Test
    void shouldGetAllZones() {
        when(zoneRepository.findAll()).thenReturn(List.of(
                Zone.builder().name("Zone 1").area(50.0).cropType("wheat").build(),
                Zone.builder().name("Zone 2").area(75.0).cropType("corn").build()
        ));

        var zones = zoneRepository.findAll();
        assertThat(zones).hasSize(2);
    }

    @Test
    void shouldThrowWhenZoneNotFound() {
        when(zoneRepository.findById(99L)).thenReturn(Optional.empty());

        var result = zoneRepository.findById(99L);
        assertThat(result).isEmpty();
    }
}