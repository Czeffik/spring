package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.PlayerService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameServiceFactory {
    public static GameService create(GameRepository gameRepository, PlayerService playerService) {
        return new GameServiceImpl(gameRepository, playerService);
    }
}
