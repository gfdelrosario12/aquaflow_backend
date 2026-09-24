package com.aquaflow.backend.controller;

import com.aquaflow.backend.api.EdgeNodeSyncController;
import com.aquaflow.backend.domain.EdgeNodeSyncService;
import com.aquaflow.backend.dto.request.ConfigSyncTriggerRequest;
import com.aquaflow.backend.dto.response.ConfigSyncStatusResponse;
import com.aquaflow.backend.entity.ConfigSyncStatus;
import com.aquaflow.backend.infrastructure.exception.GlobalExceptionHandler;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EdgeNodeSyncControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EdgeNodeSyncService edgeNodeSyncService;

    @InjectMocks
    private EdgeNodeSyncController edgeNodeSyncController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(edgeNodeSyncController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldGetSyncStatusSuccessfully() throws Exception {
        ConfigSyncStatusResponse response = ConfigSyncStatusResponse.builder()
                .edgeNodeId(1L)
                .nodeIdentifier("NODE-01")
                .configVersion(2L)
                .status(ConfigSyncStatus.ACKNOWLEDGED)
                .correlationId("corr-123")
                .build();

        when(edgeNodeSyncService.getSyncStatus(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/nodes/1/config-sync/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.edgeNodeId").value(1))
                .andExpect(jsonPath("$.configVersion").value(2))
                .andExpect(jsonPath("$.status").value("ACKNOWLEDGED"))
                .andExpect(jsonPath("$.correlationId").value("corr-123"));
    }

    @Test
    void shouldTriggerSyncSuccessfully() throws Exception {
        ConfigSyncTriggerRequest request = ConfigSyncTriggerRequest.builder()
                .reason("Manual sync test")
                .build();

        ConfigSyncStatusResponse response = ConfigSyncStatusResponse.builder()
                .edgeNodeId(1L)
                .nodeIdentifier("NODE-01")
                .configVersion(2L)
                .status(ConfigSyncStatus.QUEUED)
                .correlationId("corr-456")
                .build();

        when(edgeNodeSyncService.triggerSyncForNode(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/nodes/1/config-sync/trigger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.edgeNodeId").value(1))
                .andExpect(jsonPath("$.status").value("QUEUED"))
                .andExpect(jsonPath("$.correlationId").value("corr-456"));
    }

    @Test
    void shouldReturn404WhenNodeNotFound() throws Exception {
        when(edgeNodeSyncService.getSyncStatus(999L))
                .thenThrow(new ResourceNotFoundException("EdgeNode not found with id: 999"));

        mockMvc.perform(get("/api/v1/nodes/999/config-sync/status"))
                .andExpect(status().isNotFound());
    }
}

