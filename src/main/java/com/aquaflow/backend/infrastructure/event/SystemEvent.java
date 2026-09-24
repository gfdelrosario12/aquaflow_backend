package com.aquaflow.backend.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemEvent {

    @Builder.Default
    private String eventId = UUID.randomUUID().toString();

    private SystemEventType eventType;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private String aggregateId;

    private Long fieldId;

    private Object payload;

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public SystemEventType getEventType() { return eventType; }
    public void setEventType(SystemEventType eventType) { this.eventType = eventType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getAggregateId() { return aggregateId; }
    public void setAggregateId(String aggregateId) { this.aggregateId = aggregateId; }
    public Long getFieldId() { return fieldId; }
    public void setFieldId(Long fieldId) { this.fieldId = fieldId; }
    public Object getPayload() { return payload; }
    public void setPayload(Object payload) { this.payload = payload; }
}

