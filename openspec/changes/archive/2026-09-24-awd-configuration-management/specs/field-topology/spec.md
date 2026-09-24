## ADDED Requirements

### Requirement: System SHALL link field topology to active AWD policy configuration version
The system SHALL associate each physical field and monitoring zone representation in field topology queries (`GET /api/v1/fields/{id}/topology`) with its active `configVersion` number.

#### Scenario: Query field topology with policy version
- **WHEN** client requests `GET /api/v1/fields/{id}/topology`
- **THEN** system includes `activeConfigVersion` in field details to allow edge sync state verification

