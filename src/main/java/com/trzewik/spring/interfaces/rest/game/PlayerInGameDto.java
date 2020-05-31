package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.PlayerInGame;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerInGameDto {
    private final String id;
    private final String name;
    private final HandDto hand;
    private final Game.Move move;

    public static PlayerInGameDto from(final PlayerInGame player) {
        return Optional.ofNullable(player)
            .map(p ->
                new PlayerInGameDto(
                    p.getPlayerId(),
                    p.getName(),
                    HandDto.from(player),
                    player.getMove()))
            .orElse(null);
    }
}
