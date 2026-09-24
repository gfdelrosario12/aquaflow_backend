package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.response.ZoneTelemetryResponse;
import com.aquaflow.backend.dto.response.ZoneTelemetryTrendResponse;

import java.time.LocalDateTime;

public interface ZoneAggregationService {

    ZoneTelemetryResponse calculateAndCacheZoneTelemetry(Long zoneId);

    ZoneTelemetryResponse getLatestZoneTelemetry(Long zoneId);

    ZoneTelemetryTrendResponse getZoneTelemetryTrend(Long fieldId, Long zoneId, LocalDateTime startTime, LocalDateTime endTime);
}

