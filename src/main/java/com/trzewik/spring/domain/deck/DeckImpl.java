package com.trzewik.spring.domain.deck;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collections;
import java.util.Stack;

@AllArgsConstructor
class DeckImpl implements Deck {
    private final Stack<Deck.Card> cards;

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public Deck.Card take() {
        return cards.pop();
    }

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    static class CardImpl implements Deck.Card {
        private final Suit suit;
        private final Rank rank;

        @Override
        public String toString() {
            return suit.getImage() + rank.getRankName();
        }

        @Override
        public boolean isAce() {
            return rank.equals(Rank.ACE);
        }
    }
}
