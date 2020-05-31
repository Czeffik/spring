package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.GameService;
import com.trzewik.spring.domain.game.PlayerServiceClient;
import com.trzewik.spring.domain.game.Result;
import com.trzewik.spring.interfaces.rest.common.ErrorDto;
import com.trzewik.spring.interfaces.rest.common.ErrorEntityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping(value = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto createGame() {
        final GameService.CreateGameCommand command = new GameService.CreateGameCommand();
        return GameDto.from(gameService.create(command));
    }

    @PostMapping(value = "/games/{gameId}/players/{playerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto addPlayer(
        @PathVariable(value = "gameId") final String gameId,
        @PathVariable(value = "playerId") final String playerId
    ) throws Game.Exception, GameRepository.GameNotFoundException, PlayerServiceClient.PlayerNotFoundException {
        final GameService.AddPlayerToGameCommand command = new GameService.AddPlayerToGameCommand(gameId, playerId);
        return GameDto.from(gameService.addPlayer(command));
    }

    @PostMapping(value = "/games/{gameId}/startGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto startGame(
        @PathVariable(value = "gameId") final String gameId
    ) throws Game.Exception, GameRepository.GameNotFoundException {
        final GameService.StartGameCommand command = new GameService.StartGameCommand(gameId);
        return GameDto.from(gameService.start(command));
    }

    @PostMapping(value = "/games/{gameId}/players/{playerId}/{move}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto makeMove(
        @PathVariable(value = "gameId") final String gameId,
        @PathVariable(value = "playerId") final String playerId,
        @PathVariable(value = "move") final Game.Move move
    ) throws Game.Exception, GameRepository.GameNotFoundException {
        final GameService.MakeGameMoveCommand command = new GameService.MakeGameMoveCommand(gameId, playerId, move);
        return GameDto.from(gameService.makeMove(command));
    }

    @GetMapping(value = "/games/{gameId}/results", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultsDto getResults(
        @PathVariable(value = "gameId") final String gameId
    ) throws Result.Exception, GameRepository.GameNotFoundException {
        final GameService.GetGameResultsCommand command = new GameService.GetGameResultsCommand(gameId);
        final List<Result> results = gameService.getResults(command);
        return ResultsDto.from(results);
    }

    @ExceptionHandler(value = {Game.Exception.class, Result.Exception.class,
        IllegalArgumentException.class, NullPointerException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorDto> handleBadRequest(Exception ex) {
        return ErrorEntityHelper.create(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value =
        {PlayerServiceClient.PlayerNotFoundException.class, GameRepository.GameNotFoundException.class}
    )
    public ResponseEntity<ErrorDto> handleNotFound(Exception ex) {
        return ErrorEntityHelper.create(ex, HttpStatus.NOT_FOUND);
    }
}
