package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.AwdThresholdConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwdThresholdConfigRepository extends JpaRepository<AwdThresholdConfig, Long> {
    List<AwdThresholdConfig> findByAutoIrrigationConfigId(Long autoIrrigationConfigId);
}

