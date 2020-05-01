package com.trzewik.spring.domain.player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
public class Player {
    private final @NonNull String id;
    private final @NonNull String name;

    Player(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    static Player createCroupier() {
        return new Player("Croupier");
    }

    @Override
    public String toString() {
        return String.format("{id=%s, name=%s}", id, name);
    }
}
