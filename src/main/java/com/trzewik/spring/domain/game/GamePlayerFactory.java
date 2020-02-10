package com.trzewik.spring.domain.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GamePlayerFactory {
    public static GamePlayer create(String playerId) {
        return create(playerId, new HashSet<>(), Game.Move.HIT);
    }

    public static GamePlayer create(String playerId, Set<Deck.Card> cards, Game.Move move) {
        return new GamePlayerImpl(playerId, cards, move);
    }
}
