package com.trzewik.spring.domain.player;

import com.trzewik.spring.domain.game.Game;

import java.util.UUID;

public class PlayerFactory {
    public static Player createPlayer(String name) {
        return new PlayerImpl(generateId(), name, Game.Move.NONE);
    }

    public static Player createCroupier() {
        return new PlayerImpl(generateId(), "Croupier", Game.Move.NONE);
    }

    private static UUID generateId() {
        return UUID.randomUUID();
    }
}
