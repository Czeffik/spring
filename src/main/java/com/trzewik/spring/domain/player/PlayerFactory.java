package com.trzewik.spring.domain.player;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.game.Game;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerFactory {
    public static Player createCroupier() {
        return createPlayer("Croupier");
    }

    public static Player createPlayer(String name) {
        return createPlayer(generateId(), name, new HashSet<>(), Game.Move.HIT);
    }

    public static Player createPlayer(String id, String name) {
        return new PlayerImpl(id, name, new HashSet<>(), null);
    }

    public static Player createPlayer(String id, String name, Set<Deck.Card> hand, Game.Move move) {
        return new PlayerImpl(id, name, hand, move);
    }

    private static String generateId() {
        return UUID.randomUUID().toString();
    }
}
