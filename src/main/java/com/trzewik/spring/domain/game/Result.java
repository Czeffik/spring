package com.trzewik.spring.domain.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Result {
    private final int place;
    private final GamePlayer player;
}
