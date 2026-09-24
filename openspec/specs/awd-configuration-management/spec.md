# awd-configuration-management Specification

## Purpose

Manages autonomous Alternate Wetting and Drying (AWD) irrigation policies, crop-growth-stage threshold sets, versioning, safety validation, change auditing, and REST API endpoints.

## Requirements

### Requirement: System SHALL store and manage field-level autonomous AWD configuration
The system SHALL manage field-level `AutoIrrigationConfig` and `AwdThresholdConfig` records specifying enabled status, max irrigation duration, min cooldown period, operating hours, target flood depth, rain delay, min confidence threshold, and crop-stage thresholds.

#### Scenario: Retrieve effective AWD configuration for a field
- **WHEN** client requests `GET /api/v1/fields/{fieldId}/awd-config`
- **THEN** system returns the current active `AutoIrrigationConfig` including version number, parameters, and crop-growth-stage thresholds

#### Scenario: Update field AWD configuration
- **WHEN** client submits `PUT /api/v1/fields/{fieldId}/awd-config` with valid policy parameters, user identity, and change reason
- **THEN** system validates bounds, increments `configVersion`, updates audit fields (`updatedBy`, `changeReason`), persists changes, and returns updated configuration response

### Requirement: System SHALL validate AWD policy parameters to prevent unsafe edge operations
The system SHALL validate all configuration parameters prior to persistence, rejecting negative durations, invalid moisture percentages, inverted flood/dry thresholds, or out-of-range operating hours.

#### Scenario: Reject invalid AWD threshold values
- **WHEN** client submits configuration with target moisture threshold > 100%, negative cooldown period, or end operating hour earlier than start hour
- **THEN** system rejects update with HTTP 400 Bad Request status and descriptive validation error details

### Requirement: System SHALL serve effective policy version required for edge node synchronization
The system SHALL treat AWD configurations as policy/configuration data—exposing the effective `configVersion` for edge nodes to synchronize—without executing irrigation decisions directly in the cloud.

#### Scenario: Edge node queries current policy version
- **WHEN** edge node queries current policy metadata for its assigned field
- **THEN** system returns effective `configVersion` and policy metadata required for local autonomous edge execution

