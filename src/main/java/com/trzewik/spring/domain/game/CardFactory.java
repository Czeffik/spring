package com.trzewik.spring.domain.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardFactory {
    public static Deck.Card create(Deck.Card.Suit suit, Deck.Card.Rank rank) {
        return new DeckImpl.CardImpl(suit, rank);
    }
}
