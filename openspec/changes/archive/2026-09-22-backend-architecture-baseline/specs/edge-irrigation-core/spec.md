## Purpose

Core irrigation logic for autonomous edge irrigation including scheduling, zone management, crop tracking, and water usage optimization.

## ADDED Requirements

### Requirement: System SHALL manage irrigation zones
The system SHALL define irrigation zones with configurable parameters including name, area, crop type, and water allocation limits.

#### Scenario: Create irrigation zone
- **WHEN** user submits zone details (name, area, crop type, water allocation)
- **THEN** system persists the zone and returns the created zone with assigned ID

### Requirement: System SHALL schedule irrigation events
The system SHALL support scheduling irrigation events for specific zones with start time, duration, water volume, and recurrence rules.

#### Scenario: Schedule irrigation event
- **WHEN** user creates an irrigation schedule for a zone
- **THEN** system validates zone capacity and persists the schedule

### Requirement: System SHALL calculate water usage
The system SHALL calculate projected and actual water usage per zone and crop type based on scheduled and completed irrigation events.

#### Scenario: Calculate zone water usage
- **WHEN** system computes water usage for a date range
- **THEN** system returns total water consumed and projected usage per zone

### Requirement: System SHALL optimize irrigation scheduling
The system SHALL optimize irrigation schedules based on weather forecasts, soil moisture data, crop water requirements, and peak water pricing periods.

#### Scenario: Optimize daily irrigation schedule
- **WHEN** system runs optimization for the next 24 hours
- **THEN** system generates an optimized schedule that minimizes water waste and cost

### Requirement: System SHALL track crop types and their water requirements
The system SHALL maintain crop type definitions including water requirements per growth stage, growing season length, and optimal conditions.

#### Scenario: Query crop water requirements
- **WHEN** user requests water requirements for a crop type
- **THEN** system returns per-stage water needs and seasonal data
