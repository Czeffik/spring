package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.common.Deck;
import com.trzewik.spring.domain.common.DeckFactory;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameFactory {
    public static Game createGame() {
        Player croupier = PlayerFactory.createCroupier();
        return createGame(
            UUID.randomUUID().toString(),
            new ArrayList<>(Arrays.asList(croupier)),
            croupier,
            DeckFactory.createDeck(),
            Game.Status.NOT_STARTED,
            null);
    }

    public static Game createGame(String id, List<Player> players, Player croupier,
                                  Deck deck, Game.Status status, Player currentPlayer) {
        return new GameImpl(id, players, croupier, deck, status, currentPlayer);
    }
}
