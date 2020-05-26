package com.trzewik.spring.domain.player;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerServiceFactory {
    public static PlayerService create(final PlayerRepository playerRepository) {
        return new PlayerServiceImpl(playerRepository);
    }
}
