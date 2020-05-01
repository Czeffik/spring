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
    private final String suit;
    private final String rank;

    public static CardDto from(Card card) {
        return new CardDto(
            card.getSuit().name(),
            card.getRank().name()
        );
    }
}
