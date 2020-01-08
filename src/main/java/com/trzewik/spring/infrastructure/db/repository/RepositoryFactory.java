package com.trzewik.spring.infrastructure.db.repository;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.PlayerGameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepositoryFactory {
    public static GameRepository createGame(GameJpaRepository jpaRepository, EntityManager entityManager) {
        return new GameRepoImpl(jpaRepository, entityManager);
    }

    public static PlayerRepository createPlayer(PlayerJpaRepository jpaRepository) {
        return new PlayerRepoImpl(jpaRepository);

    }

    public static PlayerGameRepository createPlayerGame(
        PlayerGameJpaRepository jpaRepository,
        EntityManager entityManager
    ) {
        return new PlayerGameRepoImpl(jpaRepository, entityManager);
    }
}
