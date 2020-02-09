package com.trzewik.spring.domain.player;

import java.util.List;

public interface PlayerService {
    Player create(String playerName);

    default String createCroupierAndGetId() {
        return createCroupier().getId();
    }

    Player createCroupier();

    default String getId(String id) throws PlayerRepository.PlayerNotFoundException {
        return get(id).getId();
    }

    Player get(String id) throws PlayerRepository.PlayerNotFoundException;

    List<Player> get(List<String> playerIds);
}
