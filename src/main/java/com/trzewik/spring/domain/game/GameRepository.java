package com.trzewik.spring.domain.game;

import java.util.Optional;

public interface GameRepository {
    void save(Game game);

    Optional<Game> findById(String id);

    default Game findGame(String id) throws GameNotFoundException {
        return findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    void update(Game game);

    class GameNotFoundException extends Exception {
        GameNotFoundException(String id) {
            super(String.format("Game with id: [%s] not found.", id));
        }
    }
}
