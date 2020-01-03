package com.trzewik.spring.domain;

import com.trzewik.spring.domain.service.GameService;
import com.trzewik.spring.domain.service.GameServiceFactory;
import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    GameService gameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        return GameServiceFactory.createService(gameRepository, playerRepository);
    }
}
