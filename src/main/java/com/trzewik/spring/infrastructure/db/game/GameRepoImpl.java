package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameRepository;
import lombok.AllArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@AllArgsConstructor
class GameRepoImpl implements GameRepository {
    private GameJpaRepository jpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Game game) {
        jpaRepository.save(new GameEntity(GameDto.from(game)));
    }

    @Override
    public Optional<Game> findById(String id) {
        Optional<GameEntity> optional = jpaRepository.findById(id);
        return optional.map(GameDto::from).map(GameDto::to);
    }

    @Transactional
    @Override
    public void update(Game game) {
        entityManager.merge(new GameEntity(GameDto.from(game)));
        entityManager.flush();
    }
}
