package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Card;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class CardDto {
    private Card.Suit suit;
    private Card.Rank rank;

    static CardDto from(final Card card) {
        return new CardDto(
            card.getSuit(),
            card.getRank()
        );
    }

    Card toCard() {
        return Card.create(this.suit, this.rank);
    }
}
