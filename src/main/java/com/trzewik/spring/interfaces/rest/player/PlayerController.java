package com.trzewik.spring.interfaces.rest.player;

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

@ResponseBody
@RequestMapping
@AllArgsConstructor
public class PlayerController {
    private PlayerService service;

    @PostMapping(value = "/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public PlayerDto createPlayer(@NonNull @RequestBody CreatePlayerForm form) {
        return PlayerDto.from(service.createPlayer(form.getName()));
    }

    @GetMapping(value = "/players/{playerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PlayerDto getResults(
        @PathVariable(value = "playerId") String playerId
    ) throws PlayerRepository.PlayerNotFoundException {
        return PlayerDto.from(service.getPlayer(playerId));
    }

    @ExceptionHandler(value = {PlayerRepository.PlayerNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(PlayerRepository.PlayerNotFoundException ex) {
        String bodyOfResponse = ex.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreatePlayerForm {
        private String name;
    }
}
