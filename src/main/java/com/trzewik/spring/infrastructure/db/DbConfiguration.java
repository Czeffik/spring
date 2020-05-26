package com.trzewik.spring.infrastructure.db;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.infrastructure.db.game.GameJpaRepository;
import com.trzewik.spring.infrastructure.db.game.GameRepositoryFactory;
import com.trzewik.spring.infrastructure.db.player.PlayerJpaRepository;
import com.trzewik.spring.infrastructure.db.player.PlayerRepositoryFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import({
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {
    "com.trzewik.spring.infrastructure.db.game",
    "com.trzewik.spring.infrastructure.db.player"
})
@EnableJpaRepositories(basePackages = {
    "com.trzewik.spring.infrastructure.db.game",
    "com.trzewik.spring.infrastructure.db.player"
})
public class DbConfiguration {
    @Bean
    GameRepository gameRepository(final GameJpaRepository gameJpaRepository) {
        return GameRepositoryFactory.create(gameJpaRepository);
    }

    @Bean
    PlayerRepository playerRepository(final PlayerJpaRepository playerJpaRepository) {
        return PlayerRepositoryFactory.create(playerJpaRepository);
    }
}
