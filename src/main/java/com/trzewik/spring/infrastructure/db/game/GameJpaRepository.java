package com.trzewik.spring.infrastructure.db.game;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameJpaRepository extends JpaRepository<GameEntity, String> {
}
