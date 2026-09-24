package com.aquaflow.backend.controller;

import com.aquaflow.backend.api.IrrigationDecisionController;
import com.aquaflow.backend.domain.IrrigationDecisionService;
import com.aquaflow.backend.dto.request.IrrigationDecisionRequest;
import com.aquaflow.backend.dto.response.FieldIrrigationStatusResponse;
import com.aquaflow.backend.dto.response.IrrigationDecisionResponse;
import com.aquaflow.backend.entity.DecisionType;
import com.aquaflow.backend.entity.TriggerReason;
import com.aquaflow.backend.infrastructure.exception.GlobalExceptionHandler;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IrrigationDecisionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IrrigationDecisionService decisionService;

    @InjectMocks
    private IrrigationDecisionController decisionController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(decisionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReportDecisionSuccessfully() throws Exception {
        IrrigationDecisionRequest request = IrrigationDecisionRequest.builder()
                .edgeNodeId(1L)
                .decisionType(DecisionType.SOIL_MOISTURE_TRIGGER)
                .triggerReason(TriggerReason.SOIL_MOISTURE_BELOW_MIN)
                .requestedDurationMinutes(30)
                .build();

        IrrigationDecisionResponse response = IrrigationDecisionResponse.builder()
                .id(100L)
                .edgeNodeId(1L)
                .decisionType(DecisionType.SOIL_MOISTURE_TRIGGER)
                .triggerReason(TriggerReason.SOIL_MOISTURE_BELOW_MIN)
                .requestedDurationMinutes(30)
                .executionStatus("COMPLETED")
                .build();

        when(decisionService.reportDecision(any(IrrigationDecisionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/irrigation/decisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.edgeNodeId").value(1))
                .andExpect(jsonPath("$.executionStatus").value("COMPLETED"));
    }

    @Test
    void shouldGetDecisionsPaged() throws Exception {
        IrrigationDecisionResponse response = IrrigationDecisionResponse.builder()
                .id(100L)
                .edgeNodeId(1L)
                .decisionType(DecisionType.SOIL_MOISTURE_TRIGGER)
                .build();

        when(decisionService.getDecisions(eq(1L), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/irrigation/decisions?edgeNodeId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(100));
    }

    @Test
    void shouldGetFieldIrrigationStatus() throws Exception {
        FieldIrrigationStatusResponse response = FieldIrrigationStatusResponse.builder()
                .fieldId(10L)
                .fieldName("Field 1")
                .isIrrigating(true)
                .activeIrrigatingNodesCount(2)
                .todayDeliveredVolumeLiters(450.0)
                .build();

        when(decisionService.getFieldIrrigationStatus(10L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/irrigation/field/10/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fieldId").value(10))
                .andExpect(jsonPath("$.isIrrigating").value(true))
                .andExpect(jsonPath("$.activeIrrigatingNodesCount").value(2));
    }

    @Test
    void shouldReturn400OnValidationError() throws Exception {
        IrrigationDecisionRequest request = IrrigationDecisionRequest.builder()
                .edgeNodeId(1L)
                .requestedDurationMinutes(-5)
                .build();

        when(decisionService.reportDecision(any(IrrigationDecisionRequest.class)))
                .thenThrow(new ValidationException("requestedDurationMinutes cannot be negative", "INVALID_DURATION"));

        mockMvc.perform(post("/api/v1/irrigation/decisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_DURATION"));
    }
}

