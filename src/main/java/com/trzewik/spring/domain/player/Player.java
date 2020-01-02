package com.trzewik.spring.domain.player;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.game.Move;

import java.util.Set;

public interface Player {
    String getId();

    String getName();

    Set<Deck.Card> getHand();

    void addCard(Deck.Card card);

    int handValue();

    boolean isLooser();

    Move getMove();

    void setMove(Move move);
}
