package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.PlayerInGame;
import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerInGameDto {
    private final String id;
    private final String name;
    private final HandDto hand;
    private final Game.Move move;

    public static PlayerInGameDto from(Player player, Map<Player, PlayerInGame> players) {
        return Optional.ofNullable(player)
            .map(p ->
                new PlayerInGameDto(
                    p.getId(),
                    p.getName(),
                    HandDto.from(players.get(player)),
                    players.get(player).getMove()))
            .orElse(null);
    }
}
