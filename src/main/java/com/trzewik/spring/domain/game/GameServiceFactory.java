package com.trzewik.spring.domain.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameServiceFactory {
    public static GameService create(
        final GameRepository gameRepository,
        final PlayerServiceClient playerServiceClient
    ) {
        return new GameServiceImpl(gameRepository, playerServiceClient);
    }
}
