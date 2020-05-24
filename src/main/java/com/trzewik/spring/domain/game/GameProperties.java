package com.trzewik.spring.domain.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class GameProperties {
    private final int maximumPlayers;

    GameProperties() {
        this.maximumPlayers = 5;
    }
}
