package com.trzewik.spring.infrastructure.db.player;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerJpaRepository extends JpaRepository<PlayerEntity, String> {
}
