package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    List<Zone> findByCropType(String cropType);
    boolean existsByName(String name);
}