package com.trzewik.spring.domain.player;

import java.util.Optional;

public interface PlayerRepository {
    void save(Player player);

    Optional<Player> findById(String id);

    default Player findPlayer(String id) throws PlayerNotFoundException {
        Optional<Player> optional = findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new PlayerRepository.PlayerNotFoundException(id);
    }

    class PlayerNotFoundException extends Exception {
        PlayerNotFoundException(String id) {
            super(String.format("Player with id: [%s] not found.", id));
        }
    }
}
