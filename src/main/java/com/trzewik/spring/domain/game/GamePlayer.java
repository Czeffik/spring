package com.trzewik.spring.domain.game;

import java.util.Set;

public interface GamePlayer {
    String getName();

    String getId();

    Set<Card> getHand();

    void addCard(Card card);

    int handValue();

    boolean isLooser();

    Move getMove();

    void setMove(Move move);
}
