package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Result;
import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultsDto {
    private List<ResultDto> results;

    public static ResultsDto from(List<Result> results, List<Player> players) {
        return new ResultsDto(
            results.stream()
                .map(r -> ResultDto.from(r, getPlayerById(players, r.getPlayer().getPlayerId())))
                .collect(Collectors.toList())
        );
    }

    private static Player getPlayerById(List<Player> players, String playerId) {
        return players.stream()
            .filter(p -> p.getId().equals(playerId))
            .findFirst()
            .orElse(null);
    }
}
