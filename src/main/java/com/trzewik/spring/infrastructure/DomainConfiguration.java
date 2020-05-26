package com.trzewik.spring.infrastructure;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.GameService;
import com.trzewik.spring.domain.game.GameServiceFactory;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.domain.player.PlayerService;
import com.trzewik.spring.domain.player.PlayerServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    GameService gameService(final GameRepository gameRepository, final PlayerService playerService) {
        return GameServiceFactory.create(gameRepository, playerService);
    }

    @Bean
    PlayerService playerService(final PlayerRepository playerRepository) {
        return PlayerServiceFactory.create(playerRepository);
    }
}
