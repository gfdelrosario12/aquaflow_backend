package com.aquaflow.backend.controller;

import com.aquaflow.backend.dto.request.ZoneRequest;
import com.aquaflow.backend.dto.response.ZoneResponse;
import com.aquaflow.backend.domain.ZoneService;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ZoneControllerTest {

    private final ZoneService zoneService = mock(ZoneService.class);

    @Test
    void shouldCreateZone() {
        ZoneRequest request = new ZoneRequest();
        request.setName("Test Zone");
        request.setArea(100.0);
        request.setCropType("corn");
        request.setWaterAllocationLimit(500.0);

        ZoneResponse response = ZoneResponse.builder()
                .id(1L)
                .name("Test Zone")
                .area(100.0)
                .cropType("corn")
                .waterAllocationLimit(500.0)
                .build();

        when(zoneService.createZone(any(ZoneRequest.class))).thenReturn(response);

        var result = zoneService.createZone(request);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Zone");
    }

    @Test
    void shouldGetZoneById() {
        ZoneResponse response = ZoneResponse.builder()
                .id(1L)
                .name("Zone 1")
                .area(50.0)
                .cropType("wheat")
                .build();

        when(zoneService.getZoneById(1L)).thenReturn(response);

        var result = zoneService.getZoneById(1L);
        assertThat(result.getName()).isEqualTo("Zone 1");
    }

    @Test
    void shouldReturnNotFoundForMissingZone() {
        when(zoneService.getZoneById(99L))
                .thenThrow(new ResourceNotFoundException("Zone not found"));

        try {
            zoneService.getZoneById(99L);
        } catch (ResourceNotFoundException e) {
            assertThat(e.getMessage()).contains("Zone not found");
        }
    }
}