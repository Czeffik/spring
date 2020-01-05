package com.trzewik.spring.infrastructure.db.repository;

import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.infrastructure.db.dao.Dao;
import com.trzewik.spring.infrastructure.db.model.PlayerEntity;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
class PlayerRepoImpl implements PlayerRepository {
    private Dao<PlayerEntity> dao;

    @Override
    public void save(Player player) {
        dao.save(PlayerEntity.from(player));
    }

    @Override
    public Optional<Player> findById(String id) {
        Optional<PlayerEntity> optional = dao.get(id);
        return optional.map(PlayerEntity::getPlayer);
    }
}
