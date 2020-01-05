package com.trzewik.spring.infrastructure.db.repository;

import com.trzewik.spring.domain.game.PlayerGameRepository;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.infrastructure.db.dao.Dao;
import com.trzewik.spring.infrastructure.db.model.PlayerGameEntity;
import com.trzewik.spring.infrastructure.db.model.PlayerGameId;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
class PlayerGameRepoImpl implements PlayerGameRepository {
    private final Dao<PlayerGameEntity> dao;

    @Override
    public void save(Player player, String gameId) {
        dao.save(PlayerGameEntity.from(gameId, player));
    }

    @Override
    public Optional<Player> findById(String gameId, String playerId) {
        Optional<PlayerGameEntity> optional = dao.get(PlayerGameId.from(gameId, playerId));
        return optional.map(PlayerGameEntity::getPlayer);
    }

    @Override
    public void update(Player player, String gameId) {
        dao.update(PlayerGameEntity.from(gameId, player));
    }
}
