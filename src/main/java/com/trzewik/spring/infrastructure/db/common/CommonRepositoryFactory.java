package com.trzewik.spring.infrastructure.db.common;

import com.trzewik.spring.domain.game.PlayerGameRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonRepositoryFactory {
    public static PlayerGameRepository create(
        PlayerGameJpaRepository jpaRepository,
        EntityManager entityManager
    ) {
        return new PlayerGameRepoImpl(jpaRepository, entityManager);
    }
}
