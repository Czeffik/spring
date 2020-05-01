package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.Data;

import java.util.List;

public interface GameService {
    Game create(Player croupier);

    Game addPlayer(String gameId, Player player) throws GameException, GameRepository.GameNotFoundException;

    Game start(String gameId) throws GameRepository.GameNotFoundException, GameException;

    Game makeMove(String gameId, String playerId, Move move)
        throws GameRepository.GameNotFoundException, GameException;

    List<Result> getResults(String gameId) throws GameRepository.GameNotFoundException, GameException;

    @Data
    class MoveForm {
        private Move move;
        private String playerId;
    }

    @Data
    class AddPlayerForm {
        private String playerId;
    }
}
