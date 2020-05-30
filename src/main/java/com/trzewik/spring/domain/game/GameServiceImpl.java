package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.domain.player.PlayerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
class GameServiceImpl implements GameService {
    private final @NonNull GameRepository gameRepo;
    private final @NonNull PlayerService playerService;

    @Override
    public Game create(@NonNull final CreateGameCommand createGameCommand) {
        log.info("Received create game command: [{}].", createGameCommand);
        final PlayerService.GetCroupierCommand command = new PlayerService.GetCroupierCommand();
        final Player croupier = playerService.getCroupier(command);
        log.info("Create game with croupier: [{}].", croupier);
        final Game game = new Game(croupier);

        log.info("Game created: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public Game addPlayer(@NonNull final GameService.AddPlayerToGameCommand addPlayerToGameCommand)
        throws Game.Exception, GameRepository.GameNotFoundException, PlayerRepository.PlayerNotFoundException {
        log.info("Received add player to game command: [{}].", addPlayerToGameCommand);

        final PlayerService.GetPlayerCommand command =
            new PlayerService.GetPlayerCommand(addPlayerToGameCommand.getPlayerId());
        final Player player = playerService.get(command);
        final Game game = gameRepo.getById(addPlayerToGameCommand.getGameId()).addPlayer(player);

        log.info("Added player to game: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public Game start(@NonNull final StartGameCommand startGameCommand)
        throws GameRepository.GameNotFoundException, Game.Exception {
        log.info("Received start game command: [{}].", startGameCommand);
        final Game game = gameRepo.getById(startGameCommand.getGameId()).start();

        log.info("Started game: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public Game makeMove(@NonNull final GameService.MakeGameMoveCommand makeGameMoveCommand)
        throws GameRepository.GameNotFoundException, Game.Exception {
        log.info("Received make game move command: [{}].", makeGameMoveCommand);

        final Game game = gameRepo.getById(makeGameMoveCommand.getGameId())
            .auction(makeGameMoveCommand.getPlayerId(), makeGameMoveCommand.getMove())
            .end();

        log.info("Move made in game: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public List<Result> getResults(@NonNull final GetGameResultsCommand getGameResultsCommand)
        throws GameRepository.GameNotFoundException, Result.Exception {
        log.info("Received get game results command: [{}].", getGameResultsCommand);
        final Game game = gameRepo.getById(getGameResultsCommand.getGameId());
        return ResultsHelper.createResults(game);
    }
}
