package com.aquaflow.backend.persistence;

import com.aquaflow.backend.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {
    Optional<Field> findByName(String name);
    boolean existsByName(String name);
}

