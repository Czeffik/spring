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
            return rank.equals(RankImpl.ACE);
        }

        @AllArgsConstructor
        @Getter
        enum RankImpl implements Deck.Card.Rank {
            ACE(1, "A"),
            KING(10, "K"),
            QUEEN(10, "Q"),
            JACK(10, "J"),
            TEN(10, "10"),
            NINE(9, "9"),
            EIGHT(8, "8"),
            SEVEN(7, "7"),
            SIX(6, "6"),
            FIVE(5, "5"),
            FOUR(4, "4"),
            THREE(3, "3"),
            TWO(2, "2");

            private final int rankValue;
            private final String rankName;
        }

        @AllArgsConstructor
        @Getter
        enum SuitImpl implements Deck.Card.Suit {
            SPADE("\u2660"),
            HEART("\u2764"),
            DIAMOND("\u2666"),
            CLUB("\u2663");

            private final String image;
        }
    }
}
