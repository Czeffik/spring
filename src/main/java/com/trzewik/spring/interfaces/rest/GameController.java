package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.board.GameService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class GameController {
    private GameService board;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String index() {
        return "{\"dupa\": \"greeting\"}";
    }

    @PostMapping(value = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public Game createGame() {
        return board.createGame();
    }

    @PostMapping(value = "/games/{gameId}/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public Game addPlayer(
        @PathVariable(value = "gameId") String gameId,
        @NonNull @RequestBody AddPlayerForm addPlayerForm
    ) throws GameException, GameRepository.GameNotFoundException {
        return board.addPlayer(gameId, addPlayerForm.getName());
    }

    @PostMapping(value = "/games/{gameId}/startGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public Game startGame(
        @PathVariable(value = "gameId") String gameId
    ) throws GameException, GameRepository.GameNotFoundException {
        return board.startGame(gameId);
    }

    @PostMapping(value = "/games/{gameId}/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public Game makeMove(
        @PathVariable(value = "gameId") String gameId,
        @NonNull @RequestBody MoveForm moveForm
    ) throws GameException, GameRepository.GameNotFoundException, PlayerRepository.PlayerNotFoundException {
        return board.makeMove(gameId, moveForm.getPlayerId(), moveForm.getMove());
    }

    @GetMapping(value = "/games/{gameId}/results", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Result> getResults(
        @PathVariable(value = "gameId") String gameId
    ) throws GameException, GameRepository.GameNotFoundException {
        return board.getGameResults(gameId);
    }

    @AllArgsConstructor
    @Getter
    static class MoveForm {
        private Game.Move move;
        private String playerId;
    }

    @AllArgsConstructor
    @Getter
    static class AddPlayerForm {
        private String name;
    }
}
