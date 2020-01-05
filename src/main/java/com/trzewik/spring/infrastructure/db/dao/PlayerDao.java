package com.trzewik.spring.infrastructure.db.dao;

import com.trzewik.spring.infrastructure.db.crud.DatabaseCrud;
import com.trzewik.spring.infrastructure.db.model.PlayerEntity;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
class PlayerDao implements Dao<PlayerEntity> {
    private final DatabaseCrud<PlayerEntity> db;

    @Override
    public Optional<PlayerEntity> get(Serializable id) {
        return Optional.ofNullable(db.get(id, PlayerEntity.class));
    }

    @Override
    public List<PlayerEntity> getAll() {
        return db.getAll("SELECT a FROM PlayerEntity a", PlayerEntity.class);
    }

    @Override
    public void save(@NonNull PlayerEntity playerEntity) {
        db.save(playerEntity);
    }

    @Override
    public void update(@NonNull PlayerEntity updated) {
        db.update(updated);
    }

    @Override
    public void delete(@NonNull PlayerEntity playerEntity) {
        db.delete(playerEntity);
    }

}
