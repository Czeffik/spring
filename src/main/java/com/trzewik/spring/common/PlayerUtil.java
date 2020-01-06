package com.trzewik.spring.common;

import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerUtil {

    public static Player findPlayer(List<Player> players, String playerId) {
        return players.stream()
            .filter(player -> player.getId().equals(playerId))
            .findFirst()
            .orElseThrow(() -> new PlayerNotFoundException(players, playerId));
    }

    public static List<Player> filterOutPlayer(List<Player> players, String playerIdToExclude) {
        return players.stream()
            .filter(player -> !player.getId().equals(playerIdToExclude))
            .collect(Collectors.toList());
    }

    public static class PlayerNotFoundException extends RuntimeException {
        PlayerNotFoundException(List<Player> players, String playerId) {
            super(String.format("Can not find player with id: [%s] in players: [%s]", playerId, players));
        }
    }
}
