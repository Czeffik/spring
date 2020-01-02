package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.deck.DeckFactory;
import com.trzewik.spring.domain.player.PlayerFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameFactory {
    public static Game createGame() {
        return new GameImpl(
            UUID.randomUUID().toString(),
            PlayerFactory.createCroupier(),
            DeckFactory.createDeck(),
            Game.Status.NOT_STARTED,
            null);
    }
}
