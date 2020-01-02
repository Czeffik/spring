package com.trzewik.spring.infrastructure.db;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryGameRepository implements GameRepository {
    private final Map<String, Game> repository = new HashMap<>();

    @Override
    public synchronized void save(Game game) {
        repository.put(game.getId(), game);
    }

    @Override
    public synchronized Optional<Game> findById(String id) {
        return Optional.ofNullable(repository.get(id));
    }
}
