package com.aquaflow.backend.repository;

import com.aquaflow.backend.entity.Zone;
import com.aquaflow.backend.persistence.ZoneRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ZoneRepositoryTest {

    private final ZoneRepository zoneRepository = mock(ZoneRepository.class);

    @Test
    void shouldCreateAndFindZone() {
        Zone zone = Zone.builder()
                .name("Test Zone")
                .area(100.0)
                .cropType("corn")
                .waterAllocationLimit(500.0)
                .build();

        when(zoneRepository.save(zone)).thenReturn(zone);

        var saved = zoneRepository.save(zone);
        assertThat(saved.getName()).isEqualTo("Test Zone");
    }

    @Test
    void shouldFindZoneById() {
        Zone zone = Zone.builder()
                .id(1L)
                .name("Zone 1")
                .area(50.0)
                .cropType("wheat")
                .build();

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));

        var result = zoneRepository.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Zone 1");
    }
}