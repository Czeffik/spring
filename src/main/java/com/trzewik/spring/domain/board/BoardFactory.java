package com.trzewik.spring.domain.board;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardFactory {
    public static GameService createBoard(GameRepository gameRepository, PlayerRepository playerRepository) {
        return new GameServiceImpl(gameRepository, playerRepository);
    }
}
