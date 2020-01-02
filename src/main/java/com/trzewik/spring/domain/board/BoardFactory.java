package com.trzewik.spring.domain.board;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardFactory {
    public static Board createBoard(GameRepository gameRepository, PlayerRepository playerRepository) {
        return new BoardImpl(gameRepository, playerRepository);
    }
}
