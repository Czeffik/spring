package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.service.GameService;
import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameException;
import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class GameController {
    private GameService service;

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String index() {
        return "INDEX";
    }

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
    ) throws GameException, GameRepository.GameNotFoundException, PlayerRepository.PlayerNotFoundException {
        return GameDto.from(service.makeMove(gameId, moveForm.getPlayerId(), moveForm.getMove()));
    }

    @GetMapping(value = "/games/{gameId}/results", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResultDto> getResults(
        @PathVariable(value = "gameId") String gameId
    ) throws GameException, GameRepository.GameNotFoundException {
        return service.getGameResults(gameId).stream()
            .map(ResultDto::from)
            .collect(Collectors.toList());
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
