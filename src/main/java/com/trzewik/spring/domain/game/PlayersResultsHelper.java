package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PlayersResultsHelper {

    //TODO what if players have exactly same hand value?
    static List<Result> createResults(List<Player> playersWithCroupier) {
        List<Player> sorted = getSortedPlayersByResults(playersWithCroupier);
        return IntStream.range(0, sorted.size())
            .mapToObj(index -> new Result(index + 1, sorted.get(index)))
            .collect(Collectors.toList());
    }

    private static List<Player> getSortedPlayersByResults(List<Player> playersWithCroupier) {
        List<Player> sorted = new ArrayList<>(getSortedWinners(playersWithCroupier));
        sorted.addAll(getSortedLosers(playersWithCroupier));
        return sorted;
    }

    private static List<Player> getSortedWinners(List<Player> playersWithCroupier) {
        return playersWithCroupier.stream()
            .filter(player -> !player.isLooser())
            .sorted(Comparator.comparing(Player::handValue).reversed())
            .collect(Collectors.toList());
    }

    private static List<Player> getSortedLosers(List<Player> playersWithCroupier) {
        return playersWithCroupier.stream()
            .filter(Player::isLooser)
            .sorted(Comparator.comparing(Player::handValue))
            .collect(Collectors.toList());
    }
}
