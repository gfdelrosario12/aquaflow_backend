## Why

AquaFlow edge nodes perform autonomous Alternate Wetting and Drying (AWD) irrigation decisions locally at the field boundary. The cloud platform must serve as the authoritative policy repository—providing field-level autonomous configuration, versioning, audit logging, and crop-stage-specific thresholds—without directly executing irrigation actions itself.

## What Changes

- **AutoIrrigationConfig & AwdThresholdConfig Management**: Entities and models representing field/zone autonomous irrigation policies (enabled state, max irrigation duration, min cooldown period, allowed operating hours, target flood depth, rain delay, min confidence threshold, and crop growth stage thresholds).
- **Configuration Validation & Safety Limits**: Enforce strict validation rules preventing unsafe or out-of-bound threshold configurations (e.g. negative durations, invalid moisture limits).
- **Configuration Versioning & Audit Logging**: Track configuration version numbers (`configVersion`), record modified-by user identity and change reason, and increment versions upon every update.
- **Effective Edge Configuration Endpoint**: Provide REST endpoints (`GET /api/v1/fields/{fieldId}/awd-config`, `PUT /api/v1/fields/{fieldId}/awd-config`) exposing the effective policy version required by edge nodes for synchronization.

## Capabilities

### New Capabilities
- `awd-configuration-management`: Manages autonomous AWD irrigation policies, crop-growth-stage threshold sets, versioning, safety validation, change auditing, and REST API endpoints.

### Modified Capabilities
- `field-topology`: Connects monitoring zone operational context to effective AWD policy version references.

## Impact

- **Entities**: `AutoIrrigationConfig`, `AwdThresholdConfig`, `CropGrowthStage`.
- **Repositories**: `AutoIrrigationConfigRepository`, `AwdThresholdConfigRepository`.
- **Services**: `AwdConfigService`, `AwdConfigServiceImpl`.
- **Controllers**: `AwdConfigController` mapped to `/api/v1/fields/{fieldId}/awd-config`.
- **DTOs**: `AutoIrrigationConfigRequest`, `AutoIrrigationConfigResponse`, `AwdThresholdConfigRequest`.

