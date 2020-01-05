package com.trzewik.spring.infrastructure.db.repository;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.PlayerGameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.infrastructure.db.dao.Dao;
import com.trzewik.spring.infrastructure.db.model.GameEntity;
import com.trzewik.spring.infrastructure.db.model.PlayerEntity;
import com.trzewik.spring.infrastructure.db.model.PlayerGameEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepositoryFactory {
    public static GameRepository createGame(Dao<GameEntity> dao) {
        return new GameRepoImpl(dao);
    }

    public static PlayerRepository createPlayer(Dao<PlayerEntity> dao) {
        return new PlayerRepoImpl(dao);

    }

    public static PlayerGameRepository createPlayerGame(Dao<PlayerGameEntity> dao) {
        return new PlayerGameRepoImpl(dao);
    }
}
