package com.trzewik.spring.domain.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

public interface PlayerService {
    Player create(CreatePlayerCommand command);

    Player getCroupier();

    Player get(String id) throws PlayerRepository.PlayerNotFoundException;

    List<Player> get(List<String> playerIds);

    @Getter
    @ToString
    @RequiredArgsConstructor
    class CreatePlayerCommand {
        private final String name;
    }
}
