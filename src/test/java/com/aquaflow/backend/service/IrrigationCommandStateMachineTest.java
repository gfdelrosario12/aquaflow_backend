package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.AuditLogService;
import com.aquaflow.backend.domain.IrrigationCommandStateMachineImpl;
import com.aquaflow.backend.entity.CommandState;
import com.aquaflow.backend.entity.CommandStateTransitionHistory;
import com.aquaflow.backend.entity.IrrigationCommandRecord;
import com.aquaflow.backend.infrastructure.exception.UnauthorizedException;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.aquaflow.backend.persistence.CommandStateTransitionHistoryRepository;
import com.aquaflow.backend.persistence.IrrigationCommandRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IrrigationCommandStateMachineTest {

    @Mock
    private IrrigationCommandRecordRepository commandRecordRepository;

    @Mock
    private CommandStateTransitionHistoryRepository historyRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private IrrigationCommandStateMachineImpl stateMachine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterCommand_Success() {
        when(commandRecordRepository.findByCorrelationId("CORR-100")).thenReturn(Optional.empty());
        when(commandRecordRepository.save(any(IrrigationCommandRecord.class))).thenAnswer(i -> i.getArgument(0));

        IrrigationCommandRecord record = stateMachine.registerCommand("CORR-100", "MANUAL_START", 1L, 10L, "OPERATOR", "Testing manual start");

        assertNotNull(record);
        assertEquals("CORR-100", record.getCorrelationId());
        assertEquals(CommandState.ACCEPTED, record.getCurrentState());
        verify(historyRepository, times(1)).save(any(CommandStateTransitionHistory.class));
    }

    @Test
    void testValidTransitionSequence() {
        IrrigationCommandRecord record = new IrrigationCommandRecord("CORR-200", "MANUAL_START", 1L, 10L, CommandState.ACCEPTED, "OPERATOR", "Reason");
        when(commandRecordRepository.findByCorrelationId("CORR-200")).thenReturn(Optional.of(record));
        when(commandRecordRepository.save(any(IrrigationCommandRecord.class))).thenAnswer(i -> i.getArgument(0));

        // ACCEPTED -> QUEUED
        record = stateMachine.transitionState("CORR-200", CommandState.QUEUED, "OPERATOR", "Queued", null, "op1", "OPERATOR");
        assertEquals(CommandState.QUEUED, record.getCurrentState());

        // QUEUED -> DOWNLINK_TRANSMITTED
        record = stateMachine.transitionState("CORR-200", CommandState.DOWNLINK_TRANSMITTED, "SYSTEM", "Sent via LoRaWAN", null, null, null);
        assertEquals(CommandState.DOWNLINK_TRANSMITTED, record.getCurrentState());

        // DOWNLINK_TRANSMITTED -> EDGE_ACKNOWLEDGED
        record = stateMachine.transitionState("CORR-200", CommandState.EDGE_ACKNOWLEDGED, "EDGE_NODE", "Ack received", "{\"rssi\":-80}", null, null);
        assertEquals(CommandState.EDGE_ACKNOWLEDGED, record.getCurrentState());

        // EDGE_ACKNOWLEDGED -> EXECUTING
        record = stateMachine.transitionState("CORR-200", CommandState.EXECUTING, "EDGE_NODE", "Valve opening", null, null, null);
        assertEquals(CommandState.EXECUTING, record.getCurrentState());

        // EXECUTING -> COMPLETED
        record = stateMachine.transitionState("CORR-200", CommandState.COMPLETED, "EDGE_NODE", "Duration elapsed", null, null, null);
        assertEquals(CommandState.COMPLETED, record.getCurrentState());
    }

    @Test
    void testRejectInvalidTransition_UnstartedCompletion() {
        IrrigationCommandRecord record = new IrrigationCommandRecord("CORR-300", "MANUAL_START", 1L, 10L, CommandState.ACCEPTED, "OPERATOR", "Reason");
        when(commandRecordRepository.findByCorrelationId("CORR-300")).thenReturn(Optional.of(record));

        ValidationException ex = assertThrows(ValidationException.class, () ->
                stateMachine.transitionState("CORR-300", CommandState.COMPLETED, "OPERATOR", "Direct completion", null, null, null)
        );

        assertTrue(ex.getMessage().contains("Invalid command state transition"));
    }

    @Test
    void testRejectInvalidTransition_RestartingCompleted() {
        IrrigationCommandRecord record = new IrrigationCommandRecord("CORR-400", "MANUAL_START", 1L, 10L, CommandState.COMPLETED, "OPERATOR", "Reason");
        when(commandRecordRepository.findByCorrelationId("CORR-400")).thenReturn(Optional.of(record));

        ValidationException ex = assertThrows(ValidationException.class, () ->
                stateMachine.transitionState("CORR-400", CommandState.EXECUTING, "OPERATOR", "Restarting", null, null, null)
        );

        assertTrue(ex.getMessage().contains("Invalid command state transition"));
    }

    @Test
    void testUnauthorizedEmergencyStopClearance() {
        IrrigationCommandRecord record = new IrrigationCommandRecord("CORR-500", "EMERGENCY_STOP", 1L, null, CommandState.QUEUED, "OPERATOR", "Stop");
        when(commandRecordRepository.findByCorrelationId("CORR-500")).thenReturn(Optional.of(record));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                stateMachine.transitionState("CORR-500", CommandState.CANCELLED, "OPERATOR", "Clearing stop", null, "op1", null)
        );

        assertTrue(ex.getMessage().contains("Explicit operator authorization"));
    }

    @Test
    void testAuthorizedEmergencyStopClearance() {
        IrrigationCommandRecord record = new IrrigationCommandRecord("CORR-600", "EMERGENCY_STOP", 1L, null, CommandState.QUEUED, "OPERATOR", "Stop");
        when(commandRecordRepository.findByCorrelationId("CORR-600")).thenReturn(Optional.of(record));
        when(commandRecordRepository.save(any(IrrigationCommandRecord.class))).thenAnswer(i -> i.getArgument(0));

        IrrigationCommandRecord updated = stateMachine.transitionState("CORR-600", CommandState.CANCELLED, "ADMIN", "Cleared by admin", null, "admin1", "ROLE_ADMIN");

        assertEquals(CommandState.CANCELLED, updated.getCurrentState());
    }

    @Test
    void testIdempotentTransition() {
        IrrigationCommandRecord record = new IrrigationCommandRecord("CORR-700", "MANUAL_START", 1L, 10L, CommandState.QUEUED, "OPERATOR", "Reason");
        when(commandRecordRepository.findByCorrelationId("CORR-700")).thenReturn(Optional.of(record));

        IrrigationCommandRecord same = stateMachine.transitionState("CORR-700", CommandState.QUEUED, "OPERATOR", "Same state", null, null, null);

        assertEquals(CommandState.QUEUED, same.getCurrentState());
        verify(commandRecordRepository, never()).save(any());
    }
}

