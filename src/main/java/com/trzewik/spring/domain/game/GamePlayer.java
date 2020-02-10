package com.trzewik.spring.domain.game;

import java.util.Set;

public interface GamePlayer {
    String getPlayerId();

    Set<Deck.Card> getHand();

    void addCard(Deck.Card card);

    int handValue();

    boolean isLooser();

    Game.Move getMove();

    void setMove(Game.Move move);
}
