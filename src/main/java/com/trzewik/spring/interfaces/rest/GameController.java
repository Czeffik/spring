package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameException;
import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.GameService;
import com.trzewik.spring.interfaces.rest.dto.GameDto;
import com.trzewik.spring.interfaces.rest.dto.PlayerDto;
import com.trzewik.spring.interfaces.rest.dto.ResultsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@RequestMapping
@AllArgsConstructor
public class GameController {
    private GameService service;

    @PostMapping(value = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto createGame() {
        return GameDto.from(service.createGame());
    }

    @PostMapping(value = "/games/{gameId}/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public PlayerDto addPlayer(
        @PathVariable(value = "gameId") String gameId,
        @NonNull @RequestBody AddPlayerForm addPlayerForm
    ) throws GameException, GameRepository.GameNotFoundException {
        return PlayerDto.from(service.addPlayer(gameId, addPlayerForm.getName()));
    }

    @PostMapping(value = "/games/{gameId}/startGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto startGame(
        @PathVariable(value = "gameId") String gameId
    ) throws GameException, GameRepository.GameNotFoundException {
        return GameDto.from(service.startGame(gameId));
    }

    @PostMapping(value = "/games/{gameId}/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto makeMove(
        @PathVariable(value = "gameId") String gameId,
        @NonNull @RequestBody MoveForm moveForm
    ) throws GameException, GameRepository.GameNotFoundException {
        return GameDto.from(service.makeMove(gameId, moveForm.getPlayerId(), moveForm.getMove()));
    }

    @GetMapping(value = "/games/{gameId}/results", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultsDto getResults(
        @PathVariable(value = "gameId") String gameId
    ) throws GameException, GameRepository.GameNotFoundException {
        return ResultsDto.from(service.getGameResults(gameId));
    }

    @ExceptionHandler(value = {GameException.class})
    public ResponseEntity<Object> handleBadRequest(GameException ex) {
        String bodyOfResponse = ex.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
        GameRepository.GameNotFoundException.class
    })
    public ResponseEntity<Object> handleNotFound(Exception ex) {
        String bodyOfResponse = ex.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class MoveForm {
        private Game.Move move;
        private String playerId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class AddPlayerForm {
        private String name;
    }
}
