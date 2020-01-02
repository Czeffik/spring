package com.trzewik.spring.domain;

import com.trzewik.spring.domain.board.GameService;
import com.trzewik.spring.domain.board.BoardFactory;
import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    GameService board(GameRepository gameRepository, PlayerRepository playerRepository) {
        return BoardFactory.createBoard(gameRepository, playerRepository);
    }
}
