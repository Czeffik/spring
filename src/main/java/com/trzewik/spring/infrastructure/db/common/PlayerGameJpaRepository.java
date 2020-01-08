package com.trzewik.spring.infrastructure.db.common;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerGameJpaRepository extends JpaRepository<PlayerGameEntity, PlayerGameId> {
}
