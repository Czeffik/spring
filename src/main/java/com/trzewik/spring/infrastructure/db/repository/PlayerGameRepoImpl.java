package com.trzewik.spring.infrastructure.db.repository;

import com.trzewik.spring.domain.game.PlayerGameRepository;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.infrastructure.db.dto.PlayerGameDto;
import com.trzewik.spring.infrastructure.db.model.PlayerGameEntity;
import com.trzewik.spring.infrastructure.db.model.PlayerGameId;
import lombok.AllArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
class PlayerGameRepoImpl implements PlayerGameRepository {
    private PlayerGameJpaRepository jpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Player player, String gameId) {
        jpaRepository.save(new PlayerGameEntity(PlayerGameDto.from(gameId, player)));
    }

    @Override
    public Optional<Player> findById(String gameId, String playerId) {
        Optional<PlayerGameEntity> optional = jpaRepository.findById(PlayerGameId.from(gameId, playerId));
        return optional.map(PlayerGameEntity::getPlayerGame).map(PlayerGameDto::to);
    }

    @Override
    @Transactional
    public void update(Player player, String gameId) {
        entityManager.merge(new PlayerGameEntity(PlayerGameDto.from(gameId, player)));
        entityManager.flush();
    }

    @Override
    @Transactional
    public void update(List<Player> players, String gameId) {
        players.stream()
            .map(player -> PlayerGameDto.from(gameId, player))
            .map(PlayerGameEntity::new)
            .forEach(player -> entityManager.merge(player));
        entityManager.flush();
    }
}
