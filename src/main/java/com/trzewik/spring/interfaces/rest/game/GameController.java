package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameException;
import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.GameService;
import com.trzewik.spring.domain.game.Result;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.domain.player.PlayerService;
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

import java.util.List;
import java.util.stream.Collectors;

@ResponseBody
@RequestMapping
@AllArgsConstructor
public class GameController {
    private GameService gameService;
    private PlayerService playerService;

    @PostMapping(value = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto createGame() {
        Game game = gameService.create(playerService.createCroupierAndGetId());
        return GameDto.from(game);
    }

    @PostMapping(value = "/games/{gameId}/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto addPlayer(
        @PathVariable(value = "gameId") String gameId,
        @NonNull @RequestBody AddPlayerForm addPlayerForm
    ) throws GameException, GameRepository.GameNotFoundException, PlayerRepository.PlayerNotFoundException {
        String playerId = playerService.getId(addPlayerForm.getPlayerId());
        return GameDto.from(gameService.addPlayer(gameId, playerId));
    }

    @PostMapping(value = "/games/{gameId}/startGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto startGame(
        @PathVariable(value = "gameId") String gameId
    ) throws GameException, GameRepository.GameNotFoundException {
        return GameDto.from(gameService.start(gameId));
    }

    @PostMapping(value = "/games/{gameId}/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameDto makeMove(
        @PathVariable(value = "gameId") String gameId,
        @NonNull @RequestBody MoveForm moveForm
    ) throws GameException, GameRepository.GameNotFoundException {
        return GameDto.from(gameService.makeMove(gameId, moveForm.getPlayerId(), moveForm.getMove()));
    }

    @GetMapping(value = "/games/{gameId}/results", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultsDto getResults(
        @PathVariable(value = "gameId") String gameId
    ) throws GameException, GameRepository.GameNotFoundException {
        List<Result> results = gameService.getResults(gameId);
        List<String> playerIds = results.stream().map(r -> r.getPlayer().getPlayerId()).collect(Collectors.toList());
        List<Player> players = playerService.get(playerIds);

        return ResultsDto.from(results, players);
    }

    @ExceptionHandler(value = {GameException.class})
    public ResponseEntity<Object> handleBadRequest(GameException ex) {
        String bodyOfResponse = ex.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
        GameRepository.GameNotFoundException.class
    })
    public ResponseEntity<Object> handleGameNotFound(Exception ex) {
        String bodyOfResponse = ex.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
        PlayerRepository.PlayerNotFoundException.class
    })
    public ResponseEntity<Object> handlePlayerNotFound(Exception ex) {
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
        private String playerId;
    }
}
