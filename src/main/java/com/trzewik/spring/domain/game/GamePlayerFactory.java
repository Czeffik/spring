package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GamePlayerFactory {
    public static GamePlayer create(Player player) {
        return create(player, new HashSet<>(), Game.Move.HIT);
    }

    public static GamePlayer create(Player player, Set<Deck.Card> cards, Game.Move move) {
        return new GamePlayerImpl(player, cards, move);
    }
}
