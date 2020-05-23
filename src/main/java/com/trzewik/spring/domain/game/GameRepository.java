package com.trzewik.spring.domain.game;

import java.util.Optional;

public interface GameRepository {
    void save(Game game);

    Optional<Game> findById(String id);

    default Game getById(String id) throws GameNotFoundException {
        return findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    class GameNotFoundException extends Exception {
        public GameNotFoundException(String id) {
            super(String.format("Game with id: [%s] not found.", id));
        }
    }
}
