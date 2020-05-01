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
}
