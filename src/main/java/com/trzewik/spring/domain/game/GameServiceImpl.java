package com.trzewik.spring.domain.game;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class GameServiceImpl implements GameService {
    private final GameRepository gameRepo;

    @Override
    public Game create(String croupierId) {
        Game game = GameFactory.createGame(croupierId);

        gameRepo.save(game);

        return game;
    }

    @Override
    public Game addPlayer(String gameId, String playerId)
        throws GameException, GameRepository.GameNotFoundException {
        Game game = gameRepo.getById(gameId);

        game.addPlayer(playerId);

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
    public Game makeMove(String gameId, String playerId, Game.Move move)
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
