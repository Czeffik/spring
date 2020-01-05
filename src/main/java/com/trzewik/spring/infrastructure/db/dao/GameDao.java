package com.trzewik.spring.infrastructure.db.dao;

import com.trzewik.spring.infrastructure.db.crud.DatabaseCrud;
import com.trzewik.spring.infrastructure.db.model.GameEntity;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
class GameDao implements Dao<GameEntity> {
    private final DatabaseCrud<GameEntity> db;

    @Override
    public Optional<GameEntity> get(Serializable id) {
        return Optional.ofNullable(db.get(id, GameEntity.class));
    }

    @Override
    public List<GameEntity> getAll() {
        return db.getAll("SELECT a FROM GameEntity a", GameEntity.class);
    }

    @Override
    public void save(@NonNull GameEntity gameEntity) {
        db.save(gameEntity);
    }

    @Override
    public void update(@NonNull GameEntity updated) {
        db.update(updated);
    }

    @Override
    public void delete(@NonNull GameEntity gameEntity) {
        db.delete(gameEntity);
    }
}
