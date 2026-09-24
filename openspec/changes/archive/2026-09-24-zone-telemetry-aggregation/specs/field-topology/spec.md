## ADDED Requirements

### Requirement: System SHALL include aggregated zone telemetry summaries in field dashboard views
The system SHALL enrich field topology and zone list representations (`GET /api/v1/fields/{id}/topology` and `GET /api/v1/fields/{fieldId}/zones`) with current aggregated zone telemetry summaries, online node status counts, and composite health scores.

#### Scenario: Query field topology with aggregated zone telemetry metrics
- **WHEN** client requests `GET /api/v1/fields/{id}/topology`
- **THEN** system includes the latest aggregated telemetry summary (water level, soil moisture, temperature, health status) for each monitoring zone in the field hierarchy

