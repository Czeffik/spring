package com.trzewik.spring.domain.game;

import java.util.List;

public interface GameService {
    Game create(String croupierId);

    Game addPlayer(String gameId, String playerId) throws GameException, GameRepository.GameNotFoundException;

    Game start(String gameId) throws GameRepository.GameNotFoundException, GameException;

    Game makeMove(String gameId, String playerId, Game.Move move)
        throws GameRepository.GameNotFoundException, GameException;

    List<Result> getResults(String gameId) throws GameRepository.GameNotFoundException, GameException;
}
