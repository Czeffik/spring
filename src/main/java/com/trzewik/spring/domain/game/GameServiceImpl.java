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
    public Game create() {
        final Player croupier = playerService.getCroupier();
        log.info("Create game with croupier: [{}].", croupier);
        final Game game = new Game(croupier);

        log.info("Game created: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public Game addPlayer(@NonNull final GameService.AddPlayerCommand addPlayerCommand)
        throws Game.Exception, GameRepository.GameNotFoundException, PlayerRepository.PlayerNotFoundException {
        log.info("Received add player command: [{}].", addPlayerCommand);

        final Player player = playerService.get(addPlayerCommand.getPlayerId());
        final Game game = gameRepo.getById(addPlayerCommand.getGameId()).addPlayer(player);

        log.info("Added player to game: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public Game start(@NonNull final String gameId) throws GameRepository.GameNotFoundException, Game.Exception {
        log.info("Start game with id: [{}].", gameId);
        final Game game = gameRepo.getById(gameId).start();

        log.info("Started game: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public Game makeMove(@NonNull final GameService.MoveCommand moveCommand)
        throws GameRepository.GameNotFoundException, Game.Exception {
        log.info("Received move command: [{}].", moveCommand);

        final Game game = gameRepo.getById(moveCommand.getGameId())
            .auction(moveCommand.getPlayerId(), moveCommand.getMove())
            .end();

        log.info("Move made in game: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public List<Result> getResults(@NonNull final String gameId)
        throws GameRepository.GameNotFoundException, Result.Exception {
        log.info("Get results for game with id: [{}].", gameId);
        final Game game = gameRepo.getById(gameId);
        return ResultsHelper.createResults(game);
    }
}
