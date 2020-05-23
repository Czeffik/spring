package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.GameRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameRepositoryFactory {
    public static GameRepository create(GameJpaRepository jpaRepository) {
        return new GameRepoImpl(jpaRepository);
    }
}
