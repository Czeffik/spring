package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Card;
import com.trzewik.spring.domain.game.GamePlayer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HandDto {
    private final Set<CardDto> cards;
    private final int handValue;

    public static HandDto from(GamePlayer player) {
        return new HandDto(
            from(player.getHand()),
            player.handValue()
        );
    }

    private static Set<CardDto> from(Set<Card> cards) {
        return cards.stream()
            .map(CardDto::from)
            .collect(Collectors.toSet());
    }
}
