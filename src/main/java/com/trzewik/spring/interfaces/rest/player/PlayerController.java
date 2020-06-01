package com.trzewik.spring.interfaces.rest.player;

import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.domain.player.PlayerService;
import com.trzewik.spring.interfaces.rest.common.ErrorDto;
import com.trzewik.spring.interfaces.rest.common.ErrorEntityHelper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService service;

    @PostMapping(value = "/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public PlayerDto createPlayer(@RequestBody final CreatePlayerForm form) {
        final PlayerService.CreatePlayerCommand command = form.toCreatePlayerCommand();
        return PlayerDto.from(service.create(command));
    }

    @GetMapping(value = "/players/{playerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PlayerDto getPlayer(
        @PathVariable(value = "playerId") final String playerId
    ) throws PlayerRepository.PlayerNotFoundException {
        final PlayerService.GetPlayerCommand command = new PlayerService.GetPlayerCommand(playerId);
        return PlayerDto.from(service.get(command));
    }

    @ExceptionHandler(value = PlayerRepository.PlayerNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFound(PlayerRepository.PlayerNotFoundException ex) {
        return ErrorEntityHelper.create(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ErrorDto> handleBadRequest(NullPointerException ex) {
        return ErrorEntityHelper.create(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorDto> handleInternalServerError(Exception ex) {
        return ErrorEntityHelper.createInternalServerError();
    }

    @Data
    static class CreatePlayerForm {
        private String name;

        PlayerService.CreatePlayerCommand toCreatePlayerCommand() {
            return new PlayerService.CreatePlayerCommand(this.name);
        }
    }
}
