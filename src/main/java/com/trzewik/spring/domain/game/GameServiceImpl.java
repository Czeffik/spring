package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
class GameServiceImpl implements GameService {
    private final @NonNull GameRepository gameRepo;

    @Override
    public Game create(@NonNull Player croupier) {
        log.info("Create game with croupier: [{}].", croupier);
        Game game = new Game(croupier, new GameProperties());

        log.info("Game created: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public Game addPlayer(@NonNull String gameId, @NonNull Player player)
        throws Game.Exception, GameRepository.GameNotFoundException {
        log.info("Add player: [{}] to game with it: [{}].", player, gameId);
        Game game = gameRepo.getById(gameId).addPlayer(player);

        log.info("Added player to game: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public Game start(@NonNull String gameId) throws GameRepository.GameNotFoundException, Game.Exception {
        log.info("Start game with id: [{}].", gameId);
        Game game = gameRepo.getById(gameId).start();

        log.info("Started game: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public Game makeMove(@NonNull String gameId, @NonNull MoveForm form)
        throws GameRepository.GameNotFoundException, Game.Exception {
        log.info("Make move: [{}] in game with id: [{}}.", form, gameId);
        Game game = gameRepo.getById(gameId)
            .auction(form.getPlayerId(), form.getMove())
            .end();

        log.info("Move made in game: [{}].", game);
        gameRepo.save(game);

        return game;
    }

    @Override
    public List<Result> getResults(@NonNull String gameId)
        throws GameRepository.GameNotFoundException, Result.Exception {
        log.info("Get results for game with id: [{}].", gameId);
        Game game = gameRepo.getById(gameId);
        return ResultsHelper.createResults(game);
    }
}
