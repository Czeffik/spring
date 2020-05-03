package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Card;
import com.trzewik.spring.domain.game.PlayerInGame;
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

    public static HandDto from(PlayerInGame player) {
        return from(player.getHand(), player.handValue());
    }

    public static HandDto from(Set<Card> hand, int handValue) {
        return new HandDto(
            from(hand),
            handValue
        );
    }

    private static Set<CardDto> from(Set<Card> cards) {
        return cards.stream()
            .map(CardDto::from)
            .collect(Collectors.toSet());
    }
}
