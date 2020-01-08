package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.infrastructure.db.game.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameJpaRepository extends JpaRepository<GameEntity, String> {
}
