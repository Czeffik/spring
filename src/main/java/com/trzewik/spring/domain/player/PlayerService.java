package com.trzewik.spring.domain.player;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

public interface PlayerService {
    Player create(CreatePlayerCommand createPlayerCommand);

    Player getCroupier(GetCroupierCommand getCroupierCommand);

    Player get(GetPlayerCommand getPlayerCommand) throws PlayerRepository.PlayerNotFoundException;

    List<Player> get(GetPlayersCommand getPlayersCommand);

    interface Command {
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    class CreatePlayerCommand implements Command {
        private final @NonNull String name;
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    class GetCroupierCommand implements Command {
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    class GetPlayerCommand implements Command {
        private final @NonNull String playerId;
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    class GetPlayersCommand implements Command {
        private final @NonNull List<String> playerIds;
    }
}
