package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.ConfigSyncStatus;
import com.aquaflow.backend.entity.DownlinkQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DownlinkQueueItemRepository extends JpaRepository<DownlinkQueueItem, Long> {
    Optional<DownlinkQueueItem> findByCorrelationId(String correlationId);
    List<DownlinkQueueItem> findByStatus(ConfigSyncStatus status);
    List<DownlinkQueueItem> findByEdgeNodeIdAndStatus(Long edgeNodeId, ConfigSyncStatus status);
}

