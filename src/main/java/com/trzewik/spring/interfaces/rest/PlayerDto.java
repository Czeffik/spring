package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PlayerDto {
    private final String id;
    private final String name;
    private final HandDto hand;
    private final Game.Move move;

    public static PlayerDto from(Player player) {
        if (player == null) {
            return null;
        }
        return new PlayerDto(
            player.getId(),
            player.getName(),
            HandDto.from(player),
            player.getMove()
        );
    }
}
