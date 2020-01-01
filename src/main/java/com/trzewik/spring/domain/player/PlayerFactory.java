package com.trzewik.spring.domain.player;

public class PlayerFactory {
    public static Player createPlayer(String name) {
        return new PlayerImpl(name);
    }

    public static Player createCroupier() {
        return new Croupier();
    }
}
