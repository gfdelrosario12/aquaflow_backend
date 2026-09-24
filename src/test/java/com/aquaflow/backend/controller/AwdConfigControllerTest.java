package com.aquaflow.backend.controller;

import com.aquaflow.backend.api.AwdConfigController;
import com.aquaflow.backend.domain.AwdConfigService;
import com.aquaflow.backend.dto.request.AutoIrrigationConfigRequest;
import com.aquaflow.backend.dto.response.AutoIrrigationConfigResponse;
import com.aquaflow.backend.infrastructure.exception.GlobalExceptionHandler;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AwdConfigControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AwdConfigService awdConfigService;

    @InjectMocks
    private AwdConfigController awdConfigController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(awdConfigController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldGetAwdConfigSuccessfully() throws Exception {
        AutoIrrigationConfigResponse response = AutoIrrigationConfigResponse.builder()
                .id(1L)
                .fieldId(10L)
                .enabled(true)
                .configVersion(1L)
                .maxDurationMinutes(120)
                .build();

        when(awdConfigService.getAwdConfig(10L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/fields/10/awd-config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fieldId").value(10))
                .andExpect(jsonPath("$.configVersion").value(1))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void shouldUpdateAwdConfigSuccessfully() throws Exception {
        AutoIrrigationConfigRequest request = AutoIrrigationConfigRequest.builder()
                .enabled(true)
                .maxDurationMinutes(180)
                .updatedBy("admin")
                .changeReason("routine update")
                .build();

        AutoIrrigationConfigResponse response = AutoIrrigationConfigResponse.builder()
                .id(1L)
                .fieldId(10L)
                .enabled(true)
                .configVersion(2L)
                .maxDurationMinutes(180)
                .updatedBy("admin")
                .changeReason("routine update")
                .build();

        when(awdConfigService.updateAwdConfig(eq(10L), any(AutoIrrigationConfigRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/fields/10/awd-config")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fieldId").value(10))
                .andExpect(jsonPath("$.configVersion").value(2))
                .andExpect(jsonPath("$.maxDurationMinutes").value(180))
                .andExpect(jsonPath("$.updatedBy").value("admin"));
    }

    @Test
    void shouldReturn400OnValidationError() throws Exception {
        AutoIrrigationConfigRequest request = AutoIrrigationConfigRequest.builder()
                .maxDurationMinutes(-5) // invalid
                .build();

        when(awdConfigService.updateAwdConfig(eq(10L), any(AutoIrrigationConfigRequest.class)))
                .thenThrow(new ValidationException("maxDurationMinutes must be between 1 and 1440 minutes", "INVALID_MAX_DURATION"));

        mockMvc.perform(put("/api/v1/fields/10/awd-config")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_MAX_DURATION"));
    }
}

