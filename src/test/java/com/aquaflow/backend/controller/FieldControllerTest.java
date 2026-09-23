package com.aquaflow.backend.controller;

import com.aquaflow.backend.api.FieldController;
import com.aquaflow.backend.domain.FieldService;
import com.aquaflow.backend.domain.FieldTopologyService;
import com.aquaflow.backend.dto.request.FieldRequest;
import com.aquaflow.backend.dto.response.FieldResponse;
import com.aquaflow.backend.dto.response.FieldTopologyResponse;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FieldControllerTest {

    @Mock
    private FieldService fieldService;

    @Mock
    private FieldTopologyService fieldTopologyService;

    private FieldController fieldController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fieldController = new FieldController(fieldService, fieldTopologyService);
    }

    @Test
    void shouldCreateField() {
        FieldRequest request = FieldRequest.builder().name("East Field").areaHectares(15.0).build();
        FieldResponse response = FieldResponse.builder().id(1L).name("East Field").areaHectares(15.0).build();

        when(fieldService.createField(any(FieldRequest.class))).thenReturn(response);

        ResponseEntity<FieldResponse> result = fieldController.createField(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getName()).isEqualTo("East Field");
    }

    @Test
    void shouldGetFieldById() {
        FieldResponse response = FieldResponse.builder().id(2L).name("West Field").build();
        when(fieldService.getFieldById(2L)).thenReturn(response);

        ResponseEntity<FieldResponse> result = fieldController.getFieldById(2L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getName()).isEqualTo("West Field");
    }

    @Test
    void shouldGetFieldTopology() {
        FieldTopologyResponse topology = FieldTopologyResponse.builder().id(1L).name("East Field").build();
        when(fieldTopologyService.getFieldTopology(1L)).thenReturn(topology);

        ResponseEntity<FieldTopologyResponse> result = fieldController.getFieldTopology(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getName()).isEqualTo("East Field");
    }
}

