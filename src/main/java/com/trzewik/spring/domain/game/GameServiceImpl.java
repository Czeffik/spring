package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
class GameServiceImpl implements GameService {
    private final @NonNull GameRepository gameRepo;

    @Override
    public Game create(@NonNull Player croupier) {
        Game game = new Game(croupier);

        gameRepo.save(game);

        return game;
    }

    @Override
    public Game addPlayer(@NonNull String gameId, @NonNull Player player)
        throws Game.Exception, GameRepository.GameNotFoundException {
        Game game = gameRepo.getById(gameId).addPlayer(player);

        gameRepo.update(game);

        return game;
    }

    @Override
    public Game start(@NonNull String gameId) throws GameRepository.GameNotFoundException, Game.Exception {
        Game game = gameRepo.getById(gameId).start();

        gameRepo.update(game);

        return game;
    }

    @Override
    public Game makeMove(@NonNull String gameId, @NonNull MoveForm form)
        throws GameRepository.GameNotFoundException, Game.Exception {
        Game game = gameRepo.getById(gameId)
            .auction(form.getPlayerId(), form.getMove())
            .end();

        gameRepo.update(game);

        return game;
    }

    @Override
    public List<Result> getResults(@NonNull String gameId)
        throws GameRepository.GameNotFoundException, Result.Exception {
        Game game = gameRepo.getById(gameId);
        return ResultsHelper.createResults(game);
    }
}
