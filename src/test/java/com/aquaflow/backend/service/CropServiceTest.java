package com.aquaflow.backend.service;

import com.aquaflow.backend.entity.Crop;
import com.aquaflow.backend.persistence.CropRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CropServiceTest {

    private final CropRepository cropRepository = mock(CropRepository.class);

    @Test
    void shouldCreateCrop() {
        var crop = Crop.builder()
                .name("Corn")
                .waterPerStage(50.0)
                .growingSeasonDays(120)
                .build();

        when(cropRepository.save(any(Crop.class))).thenReturn(crop);

        assertThat(crop.getName()).isEqualTo("Corn");
        assertThat(crop.getWaterPerStage()).isEqualTo(50.0);
    }

    @Test
    void shouldFindCropByName() {
        var crop = Crop.builder()
                .id(1L)
                .name("Corn")
                .waterPerStage(50.0)
                .build();

        when(cropRepository.findByName("Corn")).thenReturn(Optional.of(crop));

        var result = cropRepository.findByName("Corn");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Corn");
    }
}