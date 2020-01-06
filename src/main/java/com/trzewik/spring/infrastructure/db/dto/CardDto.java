package com.trzewik.spring.infrastructure.db.dto;

import com.trzewik.spring.domain.deck.CardFactory;
import com.trzewik.spring.domain.deck.Deck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CardDto {
    private final Deck.Card.Suit suit;
    private final Deck.Card.Rank rank;

    public static CardDto from(Deck.Card card) {
        return new CardDto(
            card.getSuit(),
            card.getRank()
        );
    }

    public static Deck.Card to(CardDto dto) {
        return CardFactory.create(dto.suit, dto.rank);
    }
}
