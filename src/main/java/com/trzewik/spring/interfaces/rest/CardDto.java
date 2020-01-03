package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.deck.Deck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CardDto {
    private final Deck.Card.Suit suit;
    private final Deck.Card.Rank rank;

    public static CardDto from(Deck.Card card) {
        return new CardDto(
            card.getSuit(),
            card.getRank()
        );
    }
}
