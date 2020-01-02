package com.trzewik.spring.domain.game;

import java.util.Optional;

public interface GameRepository {
    void save(Game game);

    Optional<Game> findById(String id);

    default Game findGame(String id) throws GameNotFoundException {
        Optional<Game> optional = findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new GameNotFoundException(id);
    }

    class GameNotFoundException extends Exception {
        GameNotFoundException(String id) {
            super(String.format("Game with id: [%s] not found.", id));
        }
    }
}
