package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
class GameRepoImpl implements GameRepository {
    private final GameJpaRepository jpaRepository;

    @Override
    @Transactional
    public void save(Game game) {
        log.info("Saving game: [{}] in repository.", game);
        jpaRepository.saveAndFlush(new GameEntity(game));
    }

    @Override
    public Optional<Game> findById(String id) {
        log.info("Looking for game with id: [{}] in repository.", id);
        Optional<GameEntity> optional = jpaRepository.findById(id);
        return optional.map(GameEntity::toGame);
    }
}
