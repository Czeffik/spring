package com.trzewik.spring.infrastructure.db.dto;

import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PlayerDto {
    private final String id;
    private final String name;

    public static PlayerDto from(Player player) {
        return new PlayerDto(
            player.getId(),
            player.getName()
        );
    }
}
