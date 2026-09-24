## 1. DTOs, Entities, and Repository Setup

- [ ] 1.1 Create `ZoneHealthStatus` enum (`OPTIMAL`, `ATTENTION_REQUIRED`, `CRITICAL`, `OFFLINE`)
- [ ] 1.2 Create `ZoneTelemetryResponse`, `ZoneTelemetryTrendResponse`, `ZoneTelemetryTrendPoint`, and `NodeTelemetryWeightRequest` DTOs
- [ ] 1.3 Create `ZoneTelemetryAggregate` entity and `ZoneTelemetryAggregateRepository`
- [x] 1.1 Create `ZoneHealthStatus` enum (`OPTIMAL`, `ATTENTION_REQUIRED`, `CRITICAL`, `OFFLINE`)
- [x] 1.2 Create `ZoneTelemetryResponse`, `ZoneTelemetryTrendResponse`, `ZoneTelemetryTrendPoint`, and `NodeTelemetryWeightRequest` DTOs
- [x] 1.3 Create `ZoneTelemetryAggregate` entity and `ZoneTelemetryAggregateRepository`

## 2. Core Zone Aggregation Service

- [ ] 2.1 Implement `ZoneAggregationService` interface in `com.aquaflow.backend.domain`
- [ ] 2.2 Implement `ZoneAggregationServiceImpl` handling active node queries, spatial weighting math, and staleness filtering
- [ ] 2.3 Implement composite zone health status evaluation rules (`OPTIMAL`, `ATTENTION_REQUIRED`, `CRITICAL`, `OFFLINE`)
- [ ] 2.4 Implement historic zone telemetry trend calculation and aggregation caching
- [x] 2.1 Implement `ZoneAggregationService` interface in `com.aquaflow.backend.domain`
- [x] 2.2 Implement `ZoneAggregationServiceImpl` handling active node queries, spatial weighting math, and staleness filtering
- [x] 2.3 Implement composite zone health status evaluation rules (`OPTIMAL`, `ATTENTION_REQUIRED`, `CRITICAL`, `OFFLINE`)
- [x] 2.4 Implement historic zone telemetry trend calculation and aggregation caching

## 3. REST API Controllers & Field Topology Integration

- [ ] 3.1 Implement `GET /api/v1/fields/{fieldId}/zones/{zoneId}/trend` endpoint in `ZoneController`
- [ ] 3.2 Implement `GET /api/v1/fields/{fieldId}/zones/{zoneId}/telemetry/latest` endpoint in `ZoneController`
- [ ] 3.3 Enrich `GET /api/v1/fields/{id}/topology` in `FieldTopologyServiceImpl` with aggregated zone telemetry summaries
- [x] 3.1 Implement `GET /api/v1/fields/{fieldId}/zones/{zoneId}/trend` endpoint in `ZoneController`
- [x] 3.2 Implement `GET /api/v1/fields/{fieldId}/zones/{zoneId}/telemetry/latest` endpoint in `ZoneController`
- [x] 3.3 Enrich `GET /api/v1/fields/{id}/topology` in `FieldTopologyServiceImpl` with aggregated zone telemetry summaries

## 4. Verification and Integration Testing

- [ ] 4.1 Write unit tests for `ZoneAggregationServiceImpl` covering spatial weighting, staleness, missing nodes, and health score calculations
- [ ] 4.2 Write unit/WebMvc tests for `ZoneController` covering trend and current telemetry endpoints
- [ ] 4.3 Run `mvn clean compile` to verify clean compilation
- [ ] 4.4 Run `mvn test` to verify all unit and integration tests pass
- [x] 4.1 Write unit tests for `ZoneAggregationServiceImpl` covering spatial weighting, staleness, missing nodes, and health score calculations
- [x] 4.2 Write unit/WebMvc tests for `ZoneController` covering trend and current telemetry endpoints
- [x] 4.3 Run `mvn clean compile` to verify clean compilation
- [x] 4.4 Run `mvn test` to verify all unit and integration tests pass

