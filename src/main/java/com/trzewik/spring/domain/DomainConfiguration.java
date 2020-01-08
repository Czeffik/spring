package com.trzewik.spring.domain;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.common.PlayerGameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.domain.game.GameService;
import com.trzewik.spring.domain.game.GameServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    GameService gameService(
        GameRepository gameRepository,
        PlayerRepository playerRepository,
        PlayerGameRepository playerGameRepository) {
        return GameServiceFactory.create(gameRepository, playerRepository, playerGameRepository);
    }
}
