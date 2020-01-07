package com.trzewik.spring.interfaces.rest.dto;

import com.trzewik.spring.domain.game.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultDto {
    private int place;
    private PlayerDto player;

    public static ResultDto from(Result result) {
        return new ResultDto(
            result.getPlace(),
            PlayerDto.from(result.getPlayer())
        );
    }
}
