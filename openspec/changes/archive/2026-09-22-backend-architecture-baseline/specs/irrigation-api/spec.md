## Purpose

Provide a versioned REST API for all autonomous edge irrigation operations including zone management, scheduling, sensor data, device control, and reporting.

## ADDED Requirements

### Requirement: System SHALL expose versioned REST endpoints
All API endpoints SHALL be prefixed with `/api/v1/` to support future versioning.

#### Scenario: Access API endpoint
- **WHEN** client calls `/api/v1/zones` endpoint
- **THEN** system returns zone data with JSON content type

### Requirement: System SHALL support CRUD operations for irrigation zones
The system SHALL provide create, read, update, and delete operations for irrigation zones via REST.

#### Scenario: Create zone via API
- **WHEN** client sends POST to `/api/v1/zones` with zone data
- **THEN** system creates zone and returns 201 with location header

### Requirement: System SHALL support CRUD operations for irrigation schedules
The system SHALL provide create, read, update, and delete operations for irrigation schedules via REST.

#### Scenario: List all schedules
- **WHEN** client sends GET to `/api/v1/schedules`
- **THEN** system returns paginated list of irrigation schedules

### Requirement: System SHALL support CRUD operations for devices
The system SHALL provide create, read, update, and delete operations for edge devices via REST.

#### Scenario: Get device details
- **WHEN** client sends GET to `/api/v1/devices/{deviceId}`
- **THEN** system returns device details including status and zone associations

### Requirement: System SHALL support sensor data queries
The system SHALL allow querying sensor data by device ID, zone, time range, and data type with pagination.

#### Scenario: Query sensor data by time range
- **WHEN** client sends GET to `/api/v1/sensors/data?deviceId=X&from=Y&to=Z`
- **THEN** system returns paginated sensor readings within the time range

### Requirement: API SHALL return standardized error responses
All error responses SHALL use a consistent format including timestamp, status, error code, and message.

#### Scenario: Resource not found error
- **WHEN** client requests a non-existent resource
- **THEN** system returns 404 with standardized error body
