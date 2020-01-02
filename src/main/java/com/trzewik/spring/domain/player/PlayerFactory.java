package com.trzewik.spring.domain.player;

import java.util.UUID;

public class PlayerFactory {
    public static Player createPlayer(String name) {
        return new PlayerImpl(generateId(), name, null);
    }

    public static Player createCroupier() {
        return new PlayerImpl(generateId(), "Croupier", null);
    }

    private static UUID generateId() {
        return UUID.randomUUID();
    }
}
