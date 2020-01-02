package com.trzewik.spring.infrastructure.db;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfiguration {
    @Bean
    GameRepository gameRepository() {
        return new InMemoryGameRepository();
    }

    @Bean
    PlayerRepository playerRepository() {
        return new InMemoryPlayerRepository();
    }
}
