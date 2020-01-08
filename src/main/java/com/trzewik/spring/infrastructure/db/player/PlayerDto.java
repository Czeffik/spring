package com.trzewik.spring.infrastructure.db.player;

import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class PlayerDto {
    private String id;
    private String name;

    public static PlayerDto from(Player player) {
        return new PlayerDto(
            player.getId(),
            player.getName()
        );
    }

    public static PlayerDto from(PlayerEntity player) {
        return new PlayerDto(
            player.getId(),
            player.getName()
        );
    }

    public static Player to(PlayerDto dto) {
        return PlayerFactory.createPlayer(
            dto.getId(),
            dto.getName()
        );
    }
}
