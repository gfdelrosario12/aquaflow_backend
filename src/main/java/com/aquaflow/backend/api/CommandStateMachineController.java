package com.aquaflow.backend.api;

import com.aquaflow.backend.domain.IrrigationCommandStateMachine;
import com.aquaflow.backend.dto.request.CommandStateTransitionRequest;
import com.aquaflow.backend.dto.response.CommandStateResponse;
import com.aquaflow.backend.dto.response.CommandTransitionHistoryResponse;
import com.aquaflow.backend.entity.CommandStateTransitionHistory;
import com.aquaflow.backend.entity.IrrigationCommandRecord;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/irrigation/commands")
public class CommandStateMachineController {

    private static final Logger log = LoggerFactory.getLogger(CommandStateMachineController.class);

    private final IrrigationCommandStateMachine stateMachine;

    public CommandStateMachineController(IrrigationCommandStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @GetMapping("/{commandId}")
    public ResponseEntity<CommandStateResponse> getCommandState(@PathVariable("commandId") String commandId) {
        log.info("GET /api/v1/irrigation/commands/{}", commandId);
        IrrigationCommandRecord record = stateMachine.getCommand(commandId);
        return ResponseEntity.ok(toCommandStateResponse(record));
    }

    @GetMapping("/{commandId}/history")
    public ResponseEntity<List<CommandTransitionHistoryResponse>> getCommandHistory(@PathVariable("commandId") String commandId) {
        log.info("GET /api/v1/irrigation/commands/{}/history", commandId);
        List<CommandStateTransitionHistory> historyList = stateMachine.getTransitionHistory(commandId);
        List<CommandTransitionHistoryResponse> responses = historyList.stream()
                .map(this::toCommandTransitionHistoryResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/field/{fieldId}")
    public ResponseEntity<List<CommandStateResponse>> getFieldCommands(@PathVariable("fieldId") Long fieldId) {
        log.info("GET /api/v1/irrigation/commands/field/{}", fieldId);
        List<IrrigationCommandRecord> records = stateMachine.getFieldCommands(fieldId);
        List<CommandStateResponse> responses = records.stream()
                .map(this::toCommandStateResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{commandId}/transition")
    public ResponseEntity<CommandStateResponse> transitionCommandState(
            @PathVariable("commandId") String commandId,
            @Valid @RequestBody CommandStateTransitionRequest request) {
        log.info("POST /api/v1/irrigation/commands/{}/transition to targetState: {}", commandId, request.getTargetState());
        IrrigationCommandRecord record = stateMachine.transitionState(
                commandId,
                request.getTargetState(),
                request.getActorSource(),
                request.getTransitionReason(),
                request.getMetadataJson(),
                request.getOperatorId(),
                request.getAuthorizationRole()
        );
        return ResponseEntity.ok(toCommandStateResponse(record));
    }

    private CommandStateResponse toCommandStateResponse(IrrigationCommandRecord record) {
        return new CommandStateResponse(
                record.getId(),
                record.getCorrelationId(),
                record.getCommandType(),
                record.getTargetFieldId(),
                record.getNodeId(),
                record.getCurrentState(),
                record.getActorSource(),
                record.getRationale(),
                record.getFailureReason(),
                record.getEdgeAckMetrics(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }

    private CommandTransitionHistoryResponse toCommandTransitionHistoryResponse(CommandStateTransitionHistory history) {
        return new CommandTransitionHistoryResponse(
                history.getId(),
                history.getCorrelationId(),
                history.getFromState(),
                history.getToState(),
                history.getActorSource(),
                history.getTransitionReason(),
                history.getMetadataJson(),
                history.getTimestamp()
        );
    }
}

