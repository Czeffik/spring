package com.trzewik.spring.domain.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameFactory {
    public static Game createGame(String croupierId) {
        return new GameImpl(croupierId);
    }

    public static Game createGame(String id, Set<GamePlayer> players, String croupierId,
                                  Deck deck, Game.Status status, String currentPlayerId) {
        return new GameImpl(id, players, croupierId, deck, status, currentPlayerId);
    }
}
