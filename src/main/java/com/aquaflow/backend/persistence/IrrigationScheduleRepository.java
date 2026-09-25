package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.IrrigationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IrrigationScheduleRepository extends JpaRepository<IrrigationSchedule, Long> {
    List<IrrigationSchedule> findByZoneIdOrderByStartTimeAsc(Long zoneId);
}