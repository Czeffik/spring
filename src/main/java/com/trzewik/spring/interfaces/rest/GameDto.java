package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.game.Game;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GameDto {
    private final String id;
    private final Game.Status status;
    private final PlayerDto currentPlayer;
    private final CroupierDto croupier;

    public static GameDto from(final Game game) {
        return new GameDto(
            game.getId(),
            game.getStatus(),
            PlayerDto.from(game.getCurrentPlayer()),
            CroupierDto.from(game.getCroupier())
        );
    }
}
