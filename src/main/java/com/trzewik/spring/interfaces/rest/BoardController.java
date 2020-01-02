package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.board.Board;
import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameException;
import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.Result;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BoardController {
    private Board board;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String index() {
        return "{\"dupa\": \"greeting\"}";
    }

    @PostMapping(value = "/createGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Game createGame() {
        return board.createGame();
    }

    @PostMapping(value = "/{gameId}/addPlayer/{playerName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Game addPlayer(
        @PathVariable(value = "gameId") String gameId,
        @PathVariable(value = "playerName") String playerName
    ) throws GameException, GameRepository.GameNotFoundException {
        return board.addPlayer(gameId, playerName);
    }

    @PostMapping(value = "/{gameId}/startGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Game startGame(
        @PathVariable(value = "gameId") String gameId
    ) throws GameException, GameRepository.GameNotFoundException {
        return board.startGame(gameId);
    }

    @PostMapping(value = "/{gameId}/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Game makeMove(
        @PathVariable(value = "gameId") String gameId,
        @NonNull @RequestBody MoveDao moveDao
    ) throws GameException, GameRepository.GameNotFoundException, PlayerRepository.PlayerNotFoundException {
        return board.makeMove(gameId, moveDao.getPlayerId(), moveDao.getMove());
    }

    @GetMapping(value = "/{gameId}/results", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Result> getResults(
        @PathVariable(value = "gameId") String gameId
    ) throws GameException, GameRepository.GameNotFoundException {
        return board.getGameResults(gameId);
    }

    @AllArgsConstructor
    @Getter
    static class MoveDao {
        private Game.Move move;
        private String playerId;
    }
}
