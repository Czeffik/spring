package com.trzewik.spring.domain.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultsHelper {

    //TODO what if players have exactly same hand value?
    public static List<Result> createResults(Set<GamePlayer> playersWithCroupier) {
        List<GamePlayer> sorted = getSortedPlayersByResults(playersWithCroupier);
        return IntStream.range(0, sorted.size())
            .mapToObj(index -> new Result(index + 1, sorted.get(index)))
            .collect(Collectors.toList());
    }

    private static List<GamePlayer> getSortedPlayersByResults(Set<GamePlayer> playersWithCroupier) {
        List<GamePlayer> sorted = new ArrayList<>(getSortedWinners(playersWithCroupier));
        sorted.addAll(getSortedLosers(playersWithCroupier));
        return sorted;
    }

    private static List<GamePlayer> getSortedWinners(Set<GamePlayer> playersWithCroupier) {
        return playersWithCroupier.stream()
            .filter(p -> !p.isLooser())
            .sorted(Comparator.comparing(GamePlayer::handValue).reversed())
            .collect(Collectors.toList());
    }

    private static List<GamePlayer> getSortedLosers(Set<GamePlayer> playersWithCroupier) {
        return playersWithCroupier.stream()
            .filter(GamePlayer::isLooser)
            .sorted(Comparator.comparing(GamePlayer::handValue))
            .collect(Collectors.toList());
    }
}
