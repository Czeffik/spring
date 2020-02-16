package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;

import java.util.Set;

public interface GamePlayer {
    Player getPlayer();

    Set<Deck.Card> getHand();

    void addCard(Deck.Card card);

    int handValue();

    boolean isLooser();

    Game.Move getMove();

    void setMove(Game.Move move);
}
