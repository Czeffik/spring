package com.trzewik.spring.infrastructure.application;

import com.trzewik.spring.domain.game.PlayerServiceClient;
import com.trzewik.spring.domain.player.PlayerService;
import com.trzewik.spring.infrastructure.application.game.PlayerServiceClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    PlayerServiceClient playerServiceClient(PlayerService playerService) {
        return new PlayerServiceClientImpl(playerService);
    }
}
