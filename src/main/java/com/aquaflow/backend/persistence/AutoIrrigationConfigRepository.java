package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.AutoIrrigationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutoIrrigationConfigRepository extends JpaRepository<AutoIrrigationConfig, Long> {
    Optional<AutoIrrigationConfig> findByFieldId(Long fieldId);
    Optional<AutoIrrigationConfig> findByEdgeNodeId(Long edgeNodeId);
}
