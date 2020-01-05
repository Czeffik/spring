package com.trzewik.spring.infrastructure.db.dao;

import com.trzewik.spring.infrastructure.db.crud.DatabaseCrud;
import com.trzewik.spring.infrastructure.db.model.GameEntity;
import com.trzewik.spring.infrastructure.db.model.PlayerEntity;
import com.trzewik.spring.infrastructure.db.model.PlayerGameEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DaoFactory {
    public static Dao<GameEntity> createGame(DatabaseCrud<GameEntity> databaseCrud) {
        return new GameDao(databaseCrud);
    }

    public static Dao<PlayerEntity> createPlayer(DatabaseCrud<PlayerEntity> databaseCrud) {
        return new PlayerDao(databaseCrud);
    }

    public static Dao<PlayerGameEntity> createPlayerGame(DatabaseCrud<PlayerGameEntity> databaseCrud) {
        return new PlayerGameDao(databaseCrud);
    }
}
