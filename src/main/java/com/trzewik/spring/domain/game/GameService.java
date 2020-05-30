package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

public interface GameService {
    Game create(CreateGameCommand createGameCommand);

    Game addPlayer(AddPlayerToGameCommand addPlayerToGameCommand)
        throws GameRepository.GameNotFoundException, Game.Exception, PlayerRepository.PlayerNotFoundException;

    Game start(StartGameCommand startGameCommand) throws GameRepository.GameNotFoundException, Game.Exception;

    Game makeMove(MakeGameMoveCommand makeGameMoveCommand)
        throws GameRepository.GameNotFoundException, Game.Exception;

    List<Result> getResults(GetGameResultsCommand getGameResultsCommand)
        throws GameRepository.GameNotFoundException, Result.Exception;

    interface Command {
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    class MakeGameMoveCommand implements Command {
        private final @NonNull String gameId;
        private final @NonNull String playerId;
        private final @NonNull Game.Move move;
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    class AddPlayerToGameCommand implements Command {
        private final @NonNull String gameId;
        private final @NonNull String playerId;
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    class CreateGameCommand implements Command {
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    class StartGameCommand implements Command {
        private final @NonNull String gameId;
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    class GetGameResultsCommand implements Command {
        private final @NonNull String gameId;
    }
}
