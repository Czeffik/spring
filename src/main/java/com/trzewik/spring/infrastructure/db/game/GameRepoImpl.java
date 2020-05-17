package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
class GameRepoImpl implements GameRepository {
    private final GameJpaRepository jpaRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void save(Game game) {
        log.info("Saving game: [{}] in repository.", game);
        jpaRepository.save(new GameEntity(GameDto.from(game)));
    }

    @Override
    public Optional<Game> findById(String id) {
        log.info("Looking for game with id: [{}] in repository.", id);
        Optional<GameEntity> optional = jpaRepository.findById(id);
        return optional.map(GameDto::from).map(GameDto::to);
    }

    @Transactional
    @Override
    public void update(Game game) {
        log.info("Updating game: [{}] in repository.", game);
        entityManager.merge(new GameEntity(GameDto.from(game)));
        entityManager.flush();
    }
}
