package com.trzewik.spring.domain.player;

import com.trzewik.spring.domain.deck.Deck;

import java.util.Set;

public interface Player {
    String getName();

    Set<Deck.Card> getHand();

    void addCard(Deck.Card card);

    int handValue();
}
