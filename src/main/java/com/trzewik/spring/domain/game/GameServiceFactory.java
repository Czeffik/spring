package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.PlayerService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameServiceFactory {
    public static GameService create(final GameRepository gameRepository, final PlayerService playerService) {
        return new GameServiceImpl(gameRepository, playerService);
    }
}
