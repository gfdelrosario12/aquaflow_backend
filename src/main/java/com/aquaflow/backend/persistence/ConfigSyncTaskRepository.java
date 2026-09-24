package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.ConfigSyncTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigSyncTaskRepository extends JpaRepository<ConfigSyncTask, Long> {
    Optional<ConfigSyncTask> findTopByEdgeNodeIdOrderByCreatedAtDesc(Long edgeNodeId);
    Optional<ConfigSyncTask> findByCorrelationId(String correlationId);
    List<ConfigSyncTask> findByEdgeNodeId(Long edgeNodeId);
}

