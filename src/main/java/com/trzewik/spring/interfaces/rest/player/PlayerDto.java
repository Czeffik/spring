package com.trzewik.spring.interfaces.rest.player;

import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerDto {
    private final String id;
    private final String name;

    public static PlayerDto from(Player player) {
        return new PlayerDto(player.getId(), player.getName());
    }
}
