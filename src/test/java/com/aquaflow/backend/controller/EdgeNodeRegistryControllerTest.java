package com.aquaflow.backend.controller;

import com.aquaflow.backend.api.EdgeNodeRegistryController;
import com.aquaflow.backend.domain.EdgeNodeRegistryService;
import com.aquaflow.backend.dto.request.*;
import com.aquaflow.backend.dto.response.*;
import com.aquaflow.backend.entity.CommunicationIdentityType;
import com.aquaflow.backend.entity.HealthState;
import com.aquaflow.backend.entity.NodeLifecycleState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class EdgeNodeRegistryControllerTest {

    @Mock
    private EdgeNodeRegistryService edgeNodeRegistryService;

    private EdgeNodeRegistryController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new EdgeNodeRegistryController(edgeNodeRegistryService);
    }

    @Test
    void shouldRegisterNode() {
        RegisterNodeRequest request = RegisterNodeRequest.builder()
                .nodeId("NODE-001")
                .identityType(CommunicationIdentityType.LORA_DEVEUI)
                .identityValue("DEV-EUI-001")
                .build();

        EdgeNodeResponse response = EdgeNodeResponse.builder()
                .id(1L)
                .nodeId("NODE-001")
                .lifecycleState(NodeLifecycleState.PROVISIONED)
                .build();

        when(edgeNodeRegistryService.registerNode(any(RegisterNodeRequest.class))).thenReturn(response);

        ResponseEntity<EdgeNodeResponse> result = controller.registerNode(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getNodeId()).isEqualTo("NODE-001");
    }

    @Test
    void shouldGetNodes() {
        EdgeNodeResponse response = EdgeNodeResponse.builder().id(1L).nodeId("NODE-001").build();
        when(edgeNodeRegistryService.getNodes(null, null, null)).thenReturn(List.of(response));

        ResponseEntity<List<EdgeNodeResponse>> result = controller.getNodes(null, null, null);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).hasSize(1);
    }

    @Test
    void shouldGetNodeById() {
        EdgeNodeResponse response = EdgeNodeResponse.builder().id(1L).nodeId("NODE-001").build();
        when(edgeNodeRegistryService.getNodeById(1L)).thenReturn(response);

        ResponseEntity<EdgeNodeResponse> result = controller.getNodeById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getNodeId()).isEqualTo("NODE-001");
    }

    @Test
    void shouldCommissionNode() {
        CommissionNodeResponse response = CommissionNodeResponse.builder()
                .id(1L)
                .nodeId("NODE-001")
                .lifecycleState(NodeLifecycleState.COMMISSIONED)
                .singleUseCommissioningToken("abc-123-token")
                .build();

        CommissionNodeRequest request = CommissionNodeRequest.builder().intervalSeconds(300).build();
        when(edgeNodeRegistryService.commissionNode(eq(1L), any(CommissionNodeRequest.class))).thenReturn(response);

        ResponseEntity<CommissionNodeResponse> result = controller.commissionNode(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getSingleUseCommissioningToken()).isEqualTo("abc-123-token");
    }

    @Test
    void shouldDecommissionNode() {
        EdgeNodeResponse response = EdgeNodeResponse.builder()
                .id(1L)
                .nodeId("NODE-001")
                .lifecycleState(NodeLifecycleState.DECOMMISSIONED)
                .build();

        when(edgeNodeRegistryService.decommissionNode(1L)).thenReturn(response);

        ResponseEntity<EdgeNodeResponse> result = controller.decommissionNode(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getLifecycleState()).isEqualTo(NodeLifecycleState.DECOMMISSIONED);
    }

    @Test
    void shouldReplaceNode() {
        ReplaceNodeRequest request = ReplaceNodeRequest.builder().replacementNodeId(2L).build();
        EdgeNodeResponse response = EdgeNodeResponse.builder()
                .id(2L)
                .nodeId("REPLACEMENT-002")
                .lifecycleState(NodeLifecycleState.COMMISSIONED)
                .build();

        when(edgeNodeRegistryService.replaceNode(eq(1L), any(ReplaceNodeRequest.class))).thenReturn(response);

        ResponseEntity<EdgeNodeResponse> result = controller.replaceNode(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(2L);
    }

    @Test
    void shouldGetNodeHealth() {
        NodeHealthResponse response = NodeHealthResponse.builder()
                .id(1L)
                .nodeId("NODE-001")
                .healthState(HealthState.HEALTHY)
                .batteryLevel(95.0)
                .build();

        when(edgeNodeRegistryService.getNodeHealth(1L)).thenReturn(response);

        ResponseEntity<NodeHealthResponse> result = controller.getNodeHealth(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getBatteryLevel()).isEqualTo(95.0);
    }
}

