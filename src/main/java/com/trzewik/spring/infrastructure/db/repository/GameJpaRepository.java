package com.trzewik.spring.infrastructure.db.repository;

import com.trzewik.spring.infrastructure.db.model.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameJpaRepository extends JpaRepository<GameEntity, String> {
}
