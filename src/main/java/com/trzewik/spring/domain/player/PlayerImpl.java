package com.trzewik.spring.domain.player;

import com.trzewik.spring.domain.deck.Deck;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
class PlayerImpl implements Player {
    private final Set<Deck.Card> hand = new HashSet<>();
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
        hand.add(card);
    }

    @Override
    public int handValue() {
        int sum = calculateHandValueWithoutAce();

        if (sum <= 11 && hasAceInHand()){
            return sum + 10;
        }

        return sum;
    }

    @Override
    public boolean isLooser() {
        return handValue() > 21;
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
