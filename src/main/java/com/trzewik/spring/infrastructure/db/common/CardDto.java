package com.trzewik.spring.infrastructure.db.common;

import com.trzewik.spring.domain.common.CardFactory;
import com.trzewik.spring.domain.common.Deck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CardDto {
    private Deck.Card.Suit suit;
    private Deck.Card.Rank rank;

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
