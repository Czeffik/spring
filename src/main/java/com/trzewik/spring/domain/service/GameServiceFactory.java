package com.trzewik.spring.domain.service;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.PlayerGameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameServiceFactory {
    public static GameService create(
        GameRepository gameRepository,
        PlayerRepository playerRepository,
        PlayerGameRepository playerGameRepository
    ) {
        return new GameServiceImpl(gameRepository, playerRepository, playerGameRepository);
    }
}
