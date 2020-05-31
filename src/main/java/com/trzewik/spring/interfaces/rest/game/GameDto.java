package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Game;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GameDto {
    private final String id;
    private final Game.Status status;
    private final PlayerInGameDto currentPlayer;
    private final CroupierDto croupier;

    public static GameDto from(final Game game) {
        return new GameDto(
            game.getId(),
            game.getStatus(),
            PlayerInGameDto.from(game.getCurrentPlayer()),
            CroupierDto.from(game.getCroupier())
        );
    }
}
