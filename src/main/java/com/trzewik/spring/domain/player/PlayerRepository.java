package com.trzewik.spring.domain.player;

import java.util.Optional;

public interface PlayerRepository {
    void save(Player player);

    Optional<Player> findById(String id);
}
