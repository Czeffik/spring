package com.trzewik.spring.domain.player;

import com.trzewik.spring.domain.deck.Deck;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
class PlayerImpl extends Contestant implements Player {
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Deck.Card> getHand() {
        return hand;
    }

    @Override
    public void addCard(Deck.Card card) {
        add(card);
    }

    @Override
    public int handValue() {
        return calculateHandValue();
    }
}
