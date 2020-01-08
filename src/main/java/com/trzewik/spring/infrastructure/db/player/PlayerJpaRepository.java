package com.trzewik.spring.infrastructure.db.player;

import com.trzewik.spring.infrastructure.db.player.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerJpaRepository extends JpaRepository<PlayerEntity, String> {
}
