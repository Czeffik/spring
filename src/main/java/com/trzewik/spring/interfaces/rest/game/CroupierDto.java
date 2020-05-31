package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Card;
import com.trzewik.spring.domain.game.PlayerInGame;
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

    public static CroupierDto from(final PlayerInGame croupier) {
        return new CroupierDto(
            croupier.getPlayerId(),
            croupier.getName(),
            getFirstCard(croupier.getHand())
        );
    }

    private static CardDto getFirstCard(final Set<Card> cards) {
        if (cards.iterator().hasNext()) {
            return CardDto.from(cards.iterator().next());
        }
        return null;
    }
}
