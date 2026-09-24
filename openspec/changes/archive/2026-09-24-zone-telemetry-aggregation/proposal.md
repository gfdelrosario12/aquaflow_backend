## Why

AquaFlow fields are partitioned into monitoring zones, but telemetry is reported by individual edge nodes. To support precision irrigation scheduling, real-time field monitoring, and automated alerts, the backend must aggregate raw multi-node telemetry readings into cohesive zone-level state metrics (water level, soil moisture, temperature, humidity, battery level, RSSI, SNR, online status, and zone health score). Currently, raw node readings are persisted independently without a unified zone telemetry aggregation service or trend query endpoint.

## What Changes

- **New Zone Aggregation Service (`ZoneAggregationService`)**: Service responsible for deriving zone-level metrics from active edge nodes assigned to each zone.
- **Aggregation Rules & Weighting**: Implement spatial weighting (if node weights are defined) and simple arithmetic averages/max/min fallbacks for unweighted nodes.
- **Deterministic Handling for Edge Cases**: Define rules for missing node telemetry, stale readings (timestamp older than configurable threshold), conflicting values, and offline nodes.
- **Zone Health & Telemetry Metrics**: Aggregate soil moisture, temperature, humidity, water level, battery, RSSI, SNR, online status node counts, and calculate composite zone health status (`OPTIMAL`, `ATTENTION_REQUIRED`, `CRITICAL`, `OFFLINE`).
- **Zone Telemetry Caching/Persistence**: Maintain latest derived zone telemetry state for efficient dashboard queries while leaving raw historical sensor readings intact.
- **Trend & Dashboard Endpoints**: Implement `GET /api/v1/fields/{fieldId}/zones/{zoneId}/trend` for zone telemetry trend analysis and extend field/zone dashboard aggregation endpoints.

## Capabilities

### New Capabilities
- `zone-telemetry-aggregation`: Derives zone-level metrics, health status, spatial weighting, stale/offline node handling, and trend time-series endpoints for monitoring zones.

### Modified Capabilities
- `field-topology`: Extends zone operational context with aggregated telemetry summary and health status.

## Impact

- **Services**: New `ZoneAggregationService` and `ZoneAggregationServiceImpl`.
- **Controllers**: Extends `ZoneController` with `GET /api/v1/fields/{fieldId}/zones/{zoneId}/trend` and updated zone telemetry dashboard endpoints.
- **DTOs**: New `ZoneTelemetryResponse`, `ZoneTelemetryTrendResponse`, `NodeTelemetryWeightRequest`, `ZoneHealthStatus`.
- **Database/Persistence**: New table or cache entity `ZoneTelemetryAggregate` (or repository projection) for storing/retrieving aggregated zone states.

