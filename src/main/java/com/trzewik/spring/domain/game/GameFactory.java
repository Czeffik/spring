package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameFactory {
    public static Game createGame(Player croupier) {
        return new GameImpl(croupier);
    }

    public static Game createGame(String id, Set<GamePlayer> players, String croupierId,
                                  Deck deck, Game.Status status, String currentPlayerId) {
        return new GameImpl(id, players, croupierId, deck, status, currentPlayerId);
    }
}
