package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.deck.DeckFactory;
import com.trzewik.spring.domain.player.PlayerFactory;

public class GameFactory {
    public static Game createGame() {
        return new GameImpl(
            PlayerFactory.createCroupier(),
            DeckFactory.createDeck(),
            Game.Status.NOT_STARTED,
            null);
    }
}
