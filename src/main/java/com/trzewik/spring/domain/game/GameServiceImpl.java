package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
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
        throws GameException, GameRepository.GameNotFoundException {
        Game game = gameRepo.getById(gameId);

        game.addPlayer(player);

        gameRepo.update(game);

        return game;
    }

    @Override
    public Game start(String gameId) throws GameRepository.GameNotFoundException, GameException {
        Game game = gameRepo.getById(gameId).startGame();

        gameRepo.update(game);

        return game;
    }

    @Override
    public Game makeMove(String gameId, String playerId, Move move)
        throws GameRepository.GameNotFoundException, GameException {
        Game game = gameRepo.getById(gameId);
        game.auction(playerId, move);

        gameRepo.update(game);

        return game;
    }

    @Override
    public List<Result> getResults(String gameId) throws GameRepository.GameNotFoundException, GameException {
        Game game = gameRepo.getById(gameId);
        return game.getResults();
    }
}
