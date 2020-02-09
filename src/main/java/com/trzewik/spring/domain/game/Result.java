package com.trzewik.spring.domain.game;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Result {
    private final int place;
    private final GamePlayer player;
}
