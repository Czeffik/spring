package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class Result {
    private final int place;
    private final Player player;
}
