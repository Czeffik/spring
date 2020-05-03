package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.Data;

import java.util.List;

public interface GameService {
    Game create(Player croupier);

    Game addPlayer(String gameId, Player player) throws GameRepository.GameNotFoundException, Game.Exception;

    Game start(String gameId) throws GameRepository.GameNotFoundException, Game.Exception;

    Game makeMove(String gameId, String playerId, Game.Move move)
        throws GameRepository.GameNotFoundException, Game.Exception;

    List<Result> getResults(String gameId) throws GameRepository.GameNotFoundException, Result.Exception;

    @Data
    class MoveForm {
        private Game.Move move;
        private String playerId;
    }

    @Data
    class AddPlayerForm {
        private String playerId;
    }
}
