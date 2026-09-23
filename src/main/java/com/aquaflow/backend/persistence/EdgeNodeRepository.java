package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.EdgeNode;
import com.aquaflow.backend.entity.HealthState;
import com.aquaflow.backend.entity.NodeLifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EdgeNodeRepository extends JpaRepository<EdgeNode, Long> {
    Optional<EdgeNode> findByNodeId(String nodeId);
    Optional<EdgeNode> findByIdentityMacAddress(String macAddress);
    Optional<EdgeNode> findByIdentitySerialNumber(String serialNumber);
    List<EdgeNode> findByMonitoringZoneId(Long monitoringZoneId);
    Page<EdgeNode> findByMonitoringZoneId(Long monitoringZoneId, Pageable pageable);
    List<EdgeNode> findByLifecycleState(NodeLifecycleState lifecycleState);
    List<EdgeNode> findByHealthState(HealthState healthState);
}

