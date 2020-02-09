package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Result;
import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultDto {
    private int place;
    private String name;
    private HandDto hand;

    public static ResultDto from(Result result, Player player) {
        return new ResultDto(
            result.getPlace(),
            player.getName(),
            HandDto.from(result.getPlayer())
        );
    }
}
