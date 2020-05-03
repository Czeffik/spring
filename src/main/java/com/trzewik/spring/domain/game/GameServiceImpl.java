package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
class GameServiceImpl implements GameService {
    private final GameRepository gameRepo;

    @Override
    public Game create(Player croupier) {
        Game game = new Game(croupier);

        gameRepo.save(game);

        return game;
    }

    @Override
    public Game addPlayer(String gameId, Player player)
        throws Game.Exception, GameRepository.GameNotFoundException {
        Game game = gameRepo.getById(gameId).addPlayer(player);

        gameRepo.update(game);

        return game;
    }

    @Override
    public Game start(String gameId) throws GameRepository.GameNotFoundException, Game.Exception {
        Game game = gameRepo.getById(gameId).start();

        gameRepo.update(game);

        return game;
    }

    @Override
    public Game makeMove(String gameId, String playerId, Game.Move move)
        throws GameRepository.GameNotFoundException, Game.Exception {
        Game game = gameRepo.getById(gameId).auction(playerId, move).end();

        gameRepo.update(game);

        return game;
    }

    @Override
    public List<Result> getResults(String gameId) throws GameRepository.GameNotFoundException, Result.Exception {
        Game game = gameRepo.getById(gameId);
        return ResultsHelper.createResults(game);
    }
}
