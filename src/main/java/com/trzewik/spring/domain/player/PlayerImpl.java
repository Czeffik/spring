package com.trzewik.spring.domain.player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
class PlayerImpl implements Player {
    private final @NonNull String id;
    private final @NonNull String name;

    @Override
    public String toString() {
        return String.format("{id=%s, name=%s}", id, name);
    }
}
