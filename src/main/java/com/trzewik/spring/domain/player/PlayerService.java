package com.trzewik.spring.domain.player;

import lombok.Data;

import java.util.List;

public interface PlayerService {
    Player create(CreatePlayerCommand command);

    Player getCroupier();

    Player get(String id) throws PlayerRepository.PlayerNotFoundException;

    List<Player> get(List<String> playerIds);

    @Data
    class CreatePlayerCommand {
        private String name;
    }
}
