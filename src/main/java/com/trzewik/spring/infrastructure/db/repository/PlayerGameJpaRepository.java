package com.trzewik.spring.infrastructure.db.repository;

import com.trzewik.spring.infrastructure.db.model.PlayerGameEntity;
import com.trzewik.spring.infrastructure.db.model.PlayerGameId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerGameJpaRepository extends JpaRepository<PlayerGameEntity, PlayerGameId> {
}
