package com.trzewik.spring.infrastructure.db.repository;

import com.trzewik.spring.infrastructure.db.model.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerJpaRepository extends JpaRepository<PlayerEntity, String> {
}
