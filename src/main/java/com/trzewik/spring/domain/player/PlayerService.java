package com.trzewik.spring.domain.player;

public interface PlayerService {
    Player createPlayer(String playerName);

    Player getPlayer(String id) throws PlayerRepository.PlayerNotFoundException;
}
