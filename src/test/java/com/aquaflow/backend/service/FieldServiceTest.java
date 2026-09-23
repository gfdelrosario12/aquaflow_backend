package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.FieldServiceImpl;
import com.aquaflow.backend.dto.request.FieldRequest;
import com.aquaflow.backend.dto.response.FieldResponse;
import com.aquaflow.backend.entity.Field;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.aquaflow.backend.persistence.FieldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FieldServiceTest {

    @Mock
    private FieldRepository fieldRepository;

    private FieldServiceImpl fieldService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fieldService = new FieldServiceImpl(fieldRepository);
    }

    @Test
    void shouldCreateFieldSuccessfully() {
        FieldRequest request = FieldRequest.builder()
                .name("North Field")
                .boundaryGeoJson("{\"type\":\"Polygon\"}")
                .areaHectares(12.5)
                .build();

        Field savedField = Field.builder()
                .id(1L)
                .name("North Field")
                .boundaryGeoJson("{\"type\":\"Polygon\"}")
                .areaHectares(12.5)
                .build();

        when(fieldRepository.existsByName("North Field")).thenReturn(false);
        when(fieldRepository.save(any(Field.class))).thenReturn(savedField);

        FieldResponse response = fieldService.createField(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("North Field");
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateField() {
        FieldRequest request = FieldRequest.builder().name("Duplicate Field").build();
        when(fieldRepository.existsByName("Duplicate Field")).thenReturn(true);

        assertThatThrownBy(() -> fieldService.createField(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void shouldGetFieldById() {
        Field field = Field.builder().id(10L).name("Field 10").areaHectares(5.0).build();
        when(fieldRepository.findById(10L)).thenReturn(Optional.of(field));

        FieldResponse response = fieldService.getFieldById(10L);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Field 10");
    }

    @Test
    void shouldThrowExceptionWhenFieldNotFound() {
        when(fieldRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fieldService.getFieldById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

