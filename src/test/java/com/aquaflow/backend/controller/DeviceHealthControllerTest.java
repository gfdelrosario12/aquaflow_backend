package com.aquaflow.backend.controller;

import com.aquaflow.backend.api.DeviceController;
import com.aquaflow.backend.domain.DeviceService;
import com.aquaflow.backend.domain.EdgeNodeRegistryService;
import com.aquaflow.backend.dto.response.NodeHealthResponse;
import com.aquaflow.backend.dto.response.NodeHealthSummaryResponse;
import com.aquaflow.backend.entity.HealthState;
import com.aquaflow.backend.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DeviceHealthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DeviceService deviceService;

    @Mock
    private EdgeNodeRegistryService edgeNodeRegistryService;

    @InjectMocks
    private DeviceController deviceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(deviceController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldGetNodeHealthSummary() throws Exception {
        NodeHealthResponse nodeHealth = NodeHealthResponse.builder()
                .id(1L)
                .nodeId("NODE-01")
                .healthState(HealthState.HEALTHY)
                .batteryLevel(90.0)
                .isTelemetryStale(false)
                .build();

        NodeHealthSummaryResponse summary = NodeHealthSummaryResponse.builder()
                .totalNodes(1)
                .healthyNodesCount(1)
                .degradedNodesCount(0)
                .criticalNodesCount(0)
                .offlineNodesCount(0)
                .staleTelemetryNodesCount(0)
                .nodes(List.of(nodeHealth))
                .build();

        when(edgeNodeRegistryService.getNodeHealthSummary()).thenReturn(summary);

        mockMvc.perform(get("/api/v1/devices/nodes/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalNodes").value(1))
                .andExpect(jsonPath("$.healthyNodesCount").value(1))
                .andExpect(jsonPath("$.nodes[0].nodeId").value("NODE-01"));
    }

    @Test
    void shouldGetNodeHealthDetail() throws Exception {
        NodeHealthResponse nodeHealth = NodeHealthResponse.builder()
                .id(10L)
                .nodeId("NODE-10")
                .healthState(HealthState.DEGRADED)
                .batteryLevel(18.0)
                .isTelemetryStale(true)
                .build();

        when(edgeNodeRegistryService.getNodeHealth(10L)).thenReturn(nodeHealth);

        mockMvc.perform(get("/api/v1/devices/nodes/10/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nodeId").value("NODE-10"))
                .andExpect(jsonPath("$.healthState").value("DEGRADED"))
                .andExpect(jsonPath("$.isTelemetryStale").value(true));
    }
}

