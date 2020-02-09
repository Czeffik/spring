package com.trzewik.spring.domain.player;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PlayerRepository {
    void save(Player player);

    Optional<Player> findById(String id);

    default Player getById(String id) throws PlayerNotFoundException {
        return findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }

    List<Player> findAllById(Collection<String> ids);

    class PlayerNotFoundException extends Exception {
        public PlayerNotFoundException(String id) {
            super(String.format("Can not find player with id: [%s] in repository.", id));
        }
    }
}
