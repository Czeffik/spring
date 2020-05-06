package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.Game.Exception;
import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.GameService;
import com.trzewik.spring.domain.game.Result;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.domain.player.PlayerService;
import lombok.AllArgsConstructor;
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

import java.util.List;

@ResponseBody
@RequestMapping
@AllArgsConstructor
public class GameController {
    private final GameService gameService;
    private final PlayerService playerService;

    @PostMapping(value = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto createGame() {
        Game game = gameService.create(playerService.getCroupier());
        return GameDto.from(game);
    }

    @PostMapping(value = "/games/{gameId}/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto addPlayer(
        @PathVariable(value = "gameId") String gameId,
        @NonNull @RequestBody GameService.AddPlayerForm addPlayerForm
    ) throws Game.Exception, GameRepository.GameNotFoundException, PlayerRepository.PlayerNotFoundException {
        Player player = playerService.get(addPlayerForm.getPlayerId());
        return GameDto.from(gameService.addPlayer(gameId, player));
    }

    @PostMapping(value = "/games/{gameId}/startGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto startGame(
        @PathVariable(value = "gameId") String gameId
    ) throws Game.Exception, GameRepository.GameNotFoundException {
        return GameDto.from(gameService.start(gameId));
    }

    @PostMapping(value = "/games/{gameId}/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto makeMove(
        @PathVariable(value = "gameId") String gameId,
        @NonNull @RequestBody GameService.MoveForm moveForm
    ) throws Game.Exception, GameRepository.GameNotFoundException {
        return GameDto.from(gameService.makeMove(gameId, moveForm.getPlayerId(), moveForm.getMove()));
    }

    @GetMapping(value = "/games/{gameId}/results", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultsDto getResults(
        @PathVariable(value = "gameId") String gameId
    ) throws Result.Exception, GameRepository.GameNotFoundException {
        List<Result> results = gameService.getResults(gameId);
        return ResultsDto.from(results);
    }

    @ExceptionHandler(value = {Game.Exception.class, Result.Exception.class})
    public ResponseEntity<Object> handleBadRequest(Exception ex) {
        String bodyOfResponse = ex.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
        GameRepository.GameNotFoundException.class, PlayerRepository.PlayerNotFoundException.class
    })
    public ResponseEntity<Object> handleGameNotFound(Exception ex) {
        String bodyOfResponse = ex.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }
}
