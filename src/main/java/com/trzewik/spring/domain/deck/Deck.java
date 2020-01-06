package com.trzewik.spring.domain.deck;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Stack;

public interface Deck {
    Stack<Card> getCards();

    void shuffle();

    Card take();

    interface Card {
        Suit getSuit();

        Rank getRank();

        boolean isAce();

        @AllArgsConstructor
        @Getter
        enum Rank {
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
        enum Suit {
            SPADE("♠"),
            HEART("❤"),
            DIAMOND("♦"),
            CLUB("♣");

            private final String image;
        }

    }
}
