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
            .orElseThrow(RuntimeException::new);
    }

    public static List<Player> filterOutPlayer(List<Player> players, String playerIdToExclude) {
        return players.stream()
            .filter(player -> !player.getId().equals(playerIdToExclude))
            .collect(Collectors.toList());
    }
}
