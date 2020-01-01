package com.trzewik.spring.domain.player;

import com.trzewik.spring.domain.deck.Deck;

import java.util.HashSet;
import java.util.Set;

abstract class Contestant {
    protected final Set<Deck.Card> hand = new HashSet<>();

    protected void add(Deck.Card card){
        hand.add(card);
    }

    protected int calculateHandValue() {
        int sum = calculateHandValueWithoutAce();

        if (sum <= 11 && hasAceInHand()){
            return sum + 10;
        }

        return sum;
    }

    private int calculateHandValueWithoutAce() {
        return hand.stream()
            .map(card -> card.getRank().getRankValue())
            .reduce(0, Integer::sum);
    }

    private boolean hasAceInHand(){
        return hand.stream()
            .anyMatch(Deck.Card::isAce);
    }
}
