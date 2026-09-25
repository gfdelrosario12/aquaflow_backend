package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CropRepository extends JpaRepository<Crop, Long> {
    Optional<Crop> findByName(String name);
    boolean existsByName(String name);
}