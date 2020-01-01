package com.trzewik.spring.domain.player;

import com.trzewik.spring.domain.deck.Deck;

import java.util.Set;

class Croupier extends Contestant implements Player{
    @Override
    public String getName() {
        return "Croupier";
    }

    @Override
    public Set<Deck.Card> getHand() {
        return hand;
    }

    @Override
    public void addCard(Deck.Card card) {
        hand.add(card);
    }

    @Override
    public int handValue() {
        return calculateHandValue();
    }
}
