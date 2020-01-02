package com.trzewik.spring.domain.player;

import com.trzewik.spring.domain.game.Move;

import java.util.UUID;

public class PlayerFactory {
    public static Player createPlayer(String name) {
        return new PlayerImpl(generateId(), name, Move.NONE);
    }

    public static Player createCroupier() {
        return new PlayerImpl(generateId(), "Croupier", Move.NONE);
    }

    private static UUID generateId() {
        return UUID.randomUUID();
    }
}
