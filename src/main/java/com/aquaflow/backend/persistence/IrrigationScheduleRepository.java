package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.IrrigationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IrrigationScheduleRepository extends JpaRepository<IrrigationSchedule, Long> {
    List<IrrigationSchedule> findByZoneIdOrderByStartTimeAsc(Long zoneId);
}