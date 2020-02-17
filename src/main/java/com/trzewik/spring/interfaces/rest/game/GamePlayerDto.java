package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.GamePlayer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GamePlayerDto {
    private final String id;
    private final String name;
    private final HandDto hand;
    private final String move;

    public static GamePlayerDto from(GamePlayer player) {
        return Optional.ofNullable(player)
            .map(p ->
                new GamePlayerDto(
                    p.getId(),
                    p.getName(),
                    HandDto.from(p),
                    p.getMove().name()))
            .orElse(null);
    }
}
