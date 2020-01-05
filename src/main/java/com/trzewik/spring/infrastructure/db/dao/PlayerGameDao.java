package com.trzewik.spring.infrastructure.db.dao;

import com.trzewik.spring.infrastructure.db.crud.DatabaseCrud;
import com.trzewik.spring.infrastructure.db.model.PlayerGameEntity;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
class PlayerGameDao implements Dao<PlayerGameEntity> {
    private final DatabaseCrud<PlayerGameEntity> db;

    @Override
    public Optional<PlayerGameEntity> get(Serializable id) {
        return Optional.ofNullable(db.get(id, PlayerGameEntity.class));
    }

    @Override
    public List<PlayerGameEntity> getAll() {
        return db.getAll("SELECT a FROM PlayerGameEntity a", PlayerGameEntity.class);
    }

    @Override
    public void save(@NonNull PlayerGameEntity playerGameEntity) {
        db.save(playerGameEntity);
    }

    @Override
    public void update(@NonNull PlayerGameEntity updated) {
        db.update(updated);
    }

    @Override
    public void delete(@NonNull PlayerGameEntity playerGameEntity) {
        db.delete(playerGameEntity);
    }
}
