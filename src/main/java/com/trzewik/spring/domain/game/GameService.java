package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

public interface GameService {
    Game create();

    Game addPlayer(AddPlayerCommand addPlayerCommand)
        throws GameRepository.GameNotFoundException, Game.Exception, PlayerRepository.PlayerNotFoundException;

    Game start(String gameId) throws GameRepository.GameNotFoundException, Game.Exception;

    Game makeMove(MoveCommand moveCommand)
        throws GameRepository.GameNotFoundException, Game.Exception;

    List<Result> getResults(String gameId) throws GameRepository.GameNotFoundException, Result.Exception;

    @Getter
    @ToString
    @RequiredArgsConstructor
    class MoveCommand {
        private final @NonNull String gameId;
        private final @NonNull String playerId;
        private final @NonNull Game.Move move;
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    class AddPlayerCommand {
        private final @NonNull String gameId;
        private final @NonNull String playerId;
    }
}
