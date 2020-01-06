package com.trzewik.spring.interfaces.rest.dto;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CroupierDto {
    private final String name;
    private final CardDto card;

    public static CroupierDto from(Player player) {
        return new CroupierDto(
            player.getName(),
            getFirstCard(player.getHand())
        );
    }

    private static CardDto getFirstCard(Set<Deck.Card> cards) {
        if (cards.iterator().hasNext()) {
            return CardDto.from(cards.iterator().next());
        }
        return null;
    }
}