package com.trzewik.spring.interfaces.rest.dto;

import com.trzewik.spring.domain.game.Game;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GameDto {
    private final String id;
    private final String status;
    private final PlayerDto currentPlayer;
    private final CroupierDto croupier;

    public static GameDto from(final Game game) {
        return new GameDto(
            game.getId(),
            game.getStatus().name(),
            PlayerDto.from(game.getCurrentPlayer()),
            CroupierDto.from(game.getCroupier())
        );
    }
}
