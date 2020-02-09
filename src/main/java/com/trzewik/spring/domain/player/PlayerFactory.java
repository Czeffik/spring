package com.trzewik.spring.domain.player;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerFactory {
    public static Player createCroupier() {
        return createPlayer("Croupier");
    }

    public static Player createPlayer(String name) {
        return createPlayer(generateId(), name);
    }

    public static Player createPlayer(String id, String name) {
        return new PlayerImpl(id, name);
    }

    private static String generateId() {
        return UUID.randomUUID().toString();
    }
}
