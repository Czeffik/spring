package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Card;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CardDto {
    private final Card.Suit suit;
    private final Card.Rank rank;

    public static CardDto from(final Card card) {
        return new CardDto(
            card.getSuit(),
            card.getRank()
        );
    }
}
