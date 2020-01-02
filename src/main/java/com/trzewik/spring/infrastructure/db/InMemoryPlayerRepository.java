package com.trzewik.spring.infrastructure.db;

import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryPlayerRepository implements PlayerRepository {
    private final Map<String, Player> repository = new HashMap<>();

    @Override
    public void save(Player player) {
        repository.put(player.getId(), player);
    }

    @Override
    public Optional<Player> findById(String id) {
        return Optional.ofNullable(repository.get(id));
    }
}
