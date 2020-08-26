package com.trzewik.spring.domain.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "create")
public class Player {
    private static final String CROUPIER_ID = "CROUPIER-ID";
    private static final String CROUPIER_NAME = "CROUPIER";
    private final @NonNull String id;
    private final @NonNull String name;

    Player(@NonNull final PlayerService.CreatePlayerCommand command) {
        this.id = UUID.randomUUID().toString();
        this.name = command.getName();
    }

    static Player createCroupier() {
        return new Player(CROUPIER_ID, CROUPIER_NAME);
    }

    @Override
    public String toString() {
        return String.format("{id=%s, name=%s}", id, name);
    }
}
