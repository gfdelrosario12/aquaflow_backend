package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByStatus(String status);
}