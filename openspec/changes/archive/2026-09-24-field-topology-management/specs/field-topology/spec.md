## Purpose

Provides domain management and read-optimized REST API services for physical fields, monitoring zones, monitoring points, crop growth stage tracking, and AWD profiles.

## ADDED Requirements

### Requirement: System SHALL manage field entities
The system SHALL support creating, listing, and retrieving details for physical `Field` entities with geographic boundary representations, total area in hectares, and audit fields.

#### Scenario: List fields with summarized zone counts
- **WHEN** client requests GET `/api/v1/fields`
- **THEN** system returns a paginated list of fields with total area, zone count, and node count summaries

#### Scenario: Retrieve field by ID
- **WHEN** client requests GET `/api/v1/fields/{id}`
- **THEN** system returns field details including boundary GeoJSON, area, and nested monitoring zone references

### Requirement: System SHALL query read-optimized field topology
The system SHALL provide a unified, read-optimized topology endpoint returning hierarchical field representations including assigned monitoring zones, monitoring points, and assigned edge nodes.

#### Scenario: Query complete field topology tree
- **WHEN** client requests GET `/api/v1/fields/{id}/topology`
- **THEN** system returns the field node containing its associated `MonitoringZone` list, each containing child `MonitoringPoint` lists and assigned `EdgeNode` status details

### Requirement: System SHALL update crop growth stage for a monitoring zone
The system SHALL support updating the current crop growth stage for a `MonitoringZone`, logging the transition in domain history.

#### Scenario: Update crop growth stage successfully
- **WHEN** client submits PUT `/api/v1/zones/{id}/crop-stage` with valid target growth stage (e.g. VEGETATIVE, REPRODUCTIVE, RIPENING)
- **THEN** system updates the zone's active growth stage, recalculates optimal moisture thresholds, and returns updated zone details

### Requirement: System SHALL update Alternate Wetting and Drying (AWD) profile
The system SHALL support configuring and updating AWD water management profiles for a `MonitoringZone`, specifying target drying depth, threshold moisture, and flood refall depth.

#### Scenario: Configure AWD profile for monitoring zone
- **WHEN** client submits PUT `/api/v1/zones/{id}/awd-profile` with valid AWD parameters
- **THEN** system persists the AWD profile on the zone and updates associated autonomous edge configuration thresholds

