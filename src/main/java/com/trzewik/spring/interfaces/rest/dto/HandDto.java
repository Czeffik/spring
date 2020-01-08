package com.trzewik.spring.interfaces.rest.dto;

import com.trzewik.spring.domain.common.Deck;
import com.trzewik.spring.domain.player.Player;
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

    public static HandDto from(Player player) {
        return new HandDto(
            from(player.getHand()),
            player.handValue()
        );
    }

    private static Set<CardDto> from(Set<Deck.Card> cards) {
        return cards.stream()
            .map(CardDto::from)
            .collect(Collectors.toSet());
    }
}
