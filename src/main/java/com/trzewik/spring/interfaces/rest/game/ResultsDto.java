package com.trzewik.spring.interfaces.rest.game;

import com.trzewik.spring.domain.game.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultsDto {
    private final List<ResultDto> results;

    public static ResultsDto from(final List<Result> results) {
        return new ResultsDto(
            results.stream()
                .map(ResultDto::from)
                .collect(Collectors.toList())
        );
    }
}
