package com.trzewik.spring.domain.player;

import lombok.Data;

import java.util.List;

public interface PlayerService {
    Player create(String playerName);

    Player createCroupier();

    Player get(String id) throws PlayerRepository.PlayerNotFoundException;

    List<Player> get(List<String> playerIds);

    @Data
    class CreatePlayerForm {
        private String name;
    }
}
