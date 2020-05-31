package com.trzewik.spring.domain.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//TODO probably should be moved to result domain
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultsHelper {

    static List<Result> createResults(final Game game) throws Result.Exception {
        validateGameStatus(game);
        return generateResults(game.getPlayers());
    }

    private static void validateGameStatus(final Game game) throws Result.Exception {
        if (!game.getStatus().isEnded()) {
            throw new Result.Exception("Results are available only when game finished. Please continue auction.");
        }
    }

    //TODO what if players have exactly same hand value?
    private static List<Result> generateResults(final List<PlayerInGame> players) {
        final List<PlayerInGame> sorted = getSortedPlayersByResults(players);
        return IntStream.range(0, sorted.size())
            .mapToObj(index -> {
                PlayerInGame player = sorted.get(index);
                return new Result(index + 1, player);
            })
            .collect(Collectors.toList());
    }

    private static List<PlayerInGame> getSortedPlayersByResults(final List<PlayerInGame> players) {
        final List<PlayerInGame> sorted = new ArrayList<>(getSortedWinners(players));
        sorted.addAll(getSortedLosers(players));
        return sorted;
    }

    private static List<PlayerInGame> getSortedWinners(final List<PlayerInGame> players) {
        return players.stream()
            .filter(p -> !p.isLooser())
            .sorted(Comparator.comparing(PlayerInGame::handValue).reversed())
            .collect(Collectors.toList());
    }

    private static List<PlayerInGame> getSortedLosers(final List<PlayerInGame> players) {
        return players.stream()
            .filter(PlayerInGame::isLooser)
            .sorted(Comparator.comparing(PlayerInGame::handValue))
            .collect(Collectors.toList());
    }
}
