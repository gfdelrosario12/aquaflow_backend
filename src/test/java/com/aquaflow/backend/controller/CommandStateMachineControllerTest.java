package com.aquaflow.backend.controller;

import com.aquaflow.backend.api.CommandStateMachineController;
import com.aquaflow.backend.domain.IrrigationCommandStateMachine;
import com.aquaflow.backend.dto.request.CommandStateTransitionRequest;
import com.aquaflow.backend.entity.CommandState;
import com.aquaflow.backend.entity.CommandStateTransitionHistory;
import com.aquaflow.backend.entity.IrrigationCommandRecord;
import com.aquaflow.backend.infrastructure.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommandStateMachineControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IrrigationCommandStateMachine stateMachine;

    @InjectMocks
    private CommandStateMachineController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testGetCommandState_Success() throws Exception {
        IrrigationCommandRecord record = new IrrigationCommandRecord("CORR-1", "MANUAL_START", 1L, 10L, CommandState.QUEUED, "OPERATOR", "Start");
        when(stateMachine.getCommand("CORR-1")).thenReturn(record);

        mockMvc.perform(get("/api/v1/irrigation/commands/CORR-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correlationId").value("CORR-1"))
                .andExpect(jsonPath("$.currentState").value("QUEUED"));
    }

    @Test
    void testGetCommandHistory_Success() throws Exception {
        CommandStateTransitionHistory h1 = new CommandStateTransitionHistory("CORR-1", null, CommandState.ACCEPTED, "OPERATOR", "Reg", null);
        CommandStateTransitionHistory h2 = new CommandStateTransitionHistory("CORR-1", CommandState.ACCEPTED, CommandState.QUEUED, "OPERATOR", "Queue", null);
        when(stateMachine.getTransitionHistory("CORR-1")).thenReturn(List.of(h1, h2));

        mockMvc.perform(get("/api/v1/irrigation/commands/CORR-1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].toState").value("ACCEPTED"))
                .andExpect(jsonPath("$[1].toState").value("QUEUED"));
    }

    @Test
    void testGetFieldCommands_Success() throws Exception {
        IrrigationCommandRecord record = new IrrigationCommandRecord("CORR-1", "MANUAL_START", 5L, 10L, CommandState.QUEUED, "OPERATOR", "Start");
        when(stateMachine.getFieldCommands(5L)).thenReturn(List.of(record));

        mockMvc.perform(get("/api/v1/irrigation/commands/field/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].targetFieldId").value(5));
    }

    @Test
    void testTransitionCommandState_Success() throws Exception {
        IrrigationCommandRecord record = new IrrigationCommandRecord("CORR-1", "MANUAL_START", 1L, 10L, CommandState.DOWNLINK_TRANSMITTED, "SYSTEM", "Sent");
        when(stateMachine.transitionState(eq("CORR-1"), eq(CommandState.DOWNLINK_TRANSMITTED), any(), any(), any(), any(), any()))
                .thenReturn(record);

        CommandStateTransitionRequest request = new CommandStateTransitionRequest(CommandState.DOWNLINK_TRANSMITTED, "SYSTEM", "Sent via LoRaWAN");

        mockMvc.perform(post("/api/v1/irrigation/commands/CORR-1/transition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentState").value("DOWNLINK_TRANSMITTED"));
    }
}

