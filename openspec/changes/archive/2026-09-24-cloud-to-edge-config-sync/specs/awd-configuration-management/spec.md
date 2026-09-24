## MODIFIED Requirements

### Requirement: System SHALL serve effective policy version required for edge node synchronization
The system SHALL treat AWD configurations as policy/configuration data—exposing the effective `configVersion` for edge nodes to synchronize and automatically triggering asynchronous LoRaWAN downlink distribution upon configuration persistence—without executing irrigation decisions directly in the cloud.

#### Scenario: Edge node queries current policy version
- **WHEN** edge node queries current policy metadata for its assigned field
- **THEN** system returns effective `configVersion` and policy metadata required for local autonomous edge execution

#### Scenario: Trigger downlink synchronization on configuration update
- **WHEN** client updates field AWD configuration via `PUT /api/v1/fields/{fieldId}/awd-config`
- **THEN** system persists the configuration update and automatically enqueues a config sync downlink task for all commissioned edge nodes assigned to the field

