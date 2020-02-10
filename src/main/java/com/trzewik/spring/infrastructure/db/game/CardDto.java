package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.CardFactory;
import com.trzewik.spring.domain.game.Deck;
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
