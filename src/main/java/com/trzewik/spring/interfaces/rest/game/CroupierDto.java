package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Deck;
import com.trzewik.spring.domain.game.GamePlayer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CroupierDto {
    private final String id;
    private final String name;
    private final CardDto card;

    public static CroupierDto from(GamePlayer croupier) {
        return new CroupierDto(
            croupier.getPlayer().getId(),
            croupier.getPlayer().getName(),
            getFirstCard(croupier.getHand())
        );
    }

    private static CardDto getFirstCard(Set<Deck.Card> cards) {
        if (cards.iterator().hasNext()) {
            return CardDto.from(cards.iterator().next());
        }
        return null;
    }
}
