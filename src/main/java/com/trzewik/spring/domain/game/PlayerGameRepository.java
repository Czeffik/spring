package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerGameRepository {
    void save(Player player, String gameId);

    Optional<Player> findById(String gameId, String playerId);

    void update(Player player, String gameId);

    default void update(List<Player> players, String gameId) {
        players.forEach(player -> update(player, gameId));
    }
}
