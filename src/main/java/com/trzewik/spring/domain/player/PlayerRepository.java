package com.trzewik.spring.domain.player;

import java.util.Optional;

public interface PlayerRepository {
    void save(Player player);

    Optional<Player> findById(String id);

    default Player getById(String id) throws PlayerNotFoundException {
        return findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }

    class PlayerNotFoundException extends Exception {
        PlayerNotFoundException(String id) {
            super(String.format("Can not find player with id: [%s] in repository.", id));
        }
    }
}
