package com.aquaflow.backend.domain;

import com.aquaflow.backend.entity.CommandState;
import com.aquaflow.backend.entity.CommandStateTransitionHistory;
import com.aquaflow.backend.entity.IrrigationCommandRecord;

import java.util.List;

public interface IrrigationCommandStateMachine {

    IrrigationCommandRecord registerCommand(String correlationId, String commandType, Long targetFieldId,
                                            Long nodeId, String actorSource, String rationale);

    IrrigationCommandRecord transitionState(String correlationId, CommandState targetState, String actorSource,
                                            String reason, String metadataJson, String operatorId, String authorizationRole);

    IrrigationCommandRecord getCommand(String correlationId);

    List<CommandStateTransitionHistory> getTransitionHistory(String correlationId);

    List<IrrigationCommandRecord> getFieldCommands(Long fieldId);
}

