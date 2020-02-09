package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;

import java.util.List;

public interface GameService {
    Game createGame();

    Player addPlayer(String gameId, String playerName) throws GameException, GameRepository.GameNotFoundException;

    Game startGame(String gameId) throws GameRepository.GameNotFoundException, GameException;

    Game makeMove(String gameId, String playerId, Game.Move move)
        throws GameRepository.GameNotFoundException, GameException;

    List<Result> getGameResults(String gameId) throws GameRepository.GameNotFoundException, GameException;
}
