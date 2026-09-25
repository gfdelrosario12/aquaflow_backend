package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.AutoIrrigationConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutoIrrigationConfigRepository extends JpaRepository<AutoIrrigationConfig, Long> {
    Optional<AutoIrrigationConfig> findByFieldId(Long fieldId);
    Optional<AutoIrrigationConfig> findByEdgeNodeId(Long edgeNodeId);
}
