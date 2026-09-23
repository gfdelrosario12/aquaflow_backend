## 1. Domain Enums and Embedded Value Objects

- [x] 1.1 Create `NodeLifecycleState`, `HealthState`, and `CommunicationIdentityType` enums in `com.aquaflow.backend.entity`
- [x] 1.2 Create `DecisionType`, `TriggerReason`, `ScheduleMode`, and `SensorType` enums in `com.aquaflow.backend.entity`
- [x] 1.3 Create `@Embeddable` `CommunicationIdentity` value object (type, identifier, macAddress, serialNumber)
- [x] 1.4 Create `@Embeddable` `NodeHealthMetrics` value object (batteryLevel, solarVoltage, signalDbms, lastHeartbeat)

## 2. JPA Domain Entities

- [x] 2.1 Create `Field` entity with audit fields (`createdAt`, `updatedAt`)
- [x] 2.2 Create `MonitoringZone` entity with relationship to `Field` and audit fields
- [x] 2.3 Create `EdgeNode` entity with relationship to `MonitoringZone`, `@Version` optimistic locking, and embedded health metrics
- [x] 2.4 Create `MonitoringPoint` entity with relationships to `MonitoringZone` and `EdgeNode`
- [x] 2.5 Create `TelemetryReading` entity with time-series timestamp and relationship to `MonitoringPoint`
- [x] 2.6 Create `IrrigationDecision` entity recording node-originated decisions with node timestamp
- [x] 2.7 Create `AutoIrrigationConfig` entity with `@Version` optimistic locking and moisture/schedule parameters
- [x] 2.8 Create `IrrigationAuditLog` entity for node lifecycle and configuration audit events

## 3. Spring Data JPA Repositories

- [x] 3.1 Create `FieldRepository` in `com.aquaflow.backend.persistence`
- [x] 3.2 Create `MonitoringZoneRepository` with field-based and name lookup methods
- [x] 3.3 Create `EdgeNodeRepository` with status, health, and hardware identity lookups
- [x] 3.4 Create `MonitoringPointRepository` with zone and edge node query methods
- [x] 3.5 Create `TelemetryReadingRepository` with paginated time-range queries (`findByMonitoringPointIdAndTimestampBetween`)
- [x] 3.6 Create `IrrigationDecisionRepository` with node decision history queries (`findByEdgeNodeIdAndNodeTimestampBetween`)
- [x] 3.7 Create `AutoIrrigationConfigRepository` for active edge node configuration lookups
- [x] 3.8 Create `IrrigationAuditLogRepository` for entity and timestamp range lookups

## 4. Flyway Database Schema Migration

- [x] 4.1 Create Flyway migration `V2__edge_irrigation_schema.sql` in `src/main/resources/db/migration`
- [x] 4.2 Define tables, primary keys, foreign keys, and default values for all edge entities
- [x] 4.3 Add composite time-series indexes (`idx_telemetry_point_time`, `idx_decision_node_time`, `idx_node_zone_status`, `idx_audit_entity_time`)
- [x] 4.4 Add uniqueness constraints for MAC addresses, serial numbers, and field/zone names

## 5. DTO Mapping & Controller Encapsulation Boundary

- [x] 5.1 Create DTO response classes (`FieldResponse`, `MonitoringZoneResponse`, `EdgeNodeResponse`, `MonitoringPointResponse`, `TelemetryReadingResponse`, `IrrigationDecisionResponse`, `AutoIrrigationConfigResponse`, `IrrigationAuditLogResponse`) in `com.aquaflow.backend.dto.response`
- [x] 5.2 Add mapping logic to `DtoMapper` in `infrastructure/util` to encapsulate entities behind DTO boundaries

## 6. Verification and Test Suite

- [x] 6.1 Create repository unit tests for `EdgeNodeRepository` and `TelemetryReadingRepository`
- [x] 6.2 Test `@Version` optimistic locking behavior on `EdgeNode` and `AutoIrrigationConfig`
- [x] 6.3 Run `mvn clean compile` to verify compilation
- [x] 6.4 Run `mvn test` to verify all unit and integration tests pass
