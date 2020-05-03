package com.trzewik.spring.domain.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Card {
    private final @NonNull Suit suit;
    private final @NonNull Rank rank;

    @Override
    public String toString() {
        return suit.getImage() + rank.getRankName();
    }

    boolean isAce() {
        return rank.equals(Rank.ACE);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Suit {
        SPADE("♠"),
        HEART("❤"),
        DIAMOND("♦"),
        CLUB("♣");

        private final String image;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Rank {
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
}
