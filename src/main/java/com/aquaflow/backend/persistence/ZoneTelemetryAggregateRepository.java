package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.ZoneTelemetryAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ZoneTelemetryAggregateRepository extends JpaRepository<ZoneTelemetryAggregate, Long> {

    Optional<ZoneTelemetryAggregate> findTopByZoneIdOrderByCalculatedAtDesc(Long zoneId);

    List<ZoneTelemetryAggregate> findByZoneIdAndCalculatedAtBetweenOrderByCalculatedAtAsc(Long zoneId, LocalDateTime start, LocalDateTime end);

    List<ZoneTelemetryAggregate> findByFieldId(Long fieldId);
}

