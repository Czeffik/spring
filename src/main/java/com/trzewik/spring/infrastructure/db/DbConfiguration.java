package com.trzewik.spring.infrastructure.db;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.PlayerGameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.infrastructure.db.crud.DatabaseCrud;
import com.trzewik.spring.infrastructure.db.crud.DatabaseCrudFactory;
import com.trzewik.spring.infrastructure.db.dao.DaoFactory;
import com.trzewik.spring.infrastructure.db.model.GameEntity;
import com.trzewik.spring.infrastructure.db.model.PlayerEntity;
import com.trzewik.spring.infrastructure.db.model.PlayerGameEntity;
import com.trzewik.spring.infrastructure.db.repository.RepositoryFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.util.Properties;

@Configuration
public class DbConfiguration {
    @Bean
    GameRepository gameRepository(DatabaseCrud<GameEntity> db) {
        return RepositoryFactory.createGame(DaoFactory.createGame(db));
    }

    @Bean
    PlayerRepository playerRepository(DatabaseCrud<PlayerEntity> db) {
        return RepositoryFactory.createPlayer(DaoFactory.createPlayer(db));
    }

    @Bean
    PlayerGameRepository playerGameRepository(DatabaseCrud<PlayerGameEntity> db) {
        return RepositoryFactory.createPlayerGame(DaoFactory.createPlayerGame(db));
    }

    @Bean
    DatabaseCrud db(SessionFactory sessionFactory) {
        return DatabaseCrudFactory.create(sessionFactory);
    }

    @Bean
    LocalSessionFactoryBean sessionFactory(Properties hibernateProperties) {
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setHibernateProperties(hibernateProperties);
        sessionFactory.setPackagesToScan("com.trzewik.spring.infrastructure.db.model");
        return sessionFactory;
    }

    @Bean
    Properties hibernateProperties(
        @Value("${db.driver.class}") String driverClass,
        @Value("${db.username}") String username,
        @Value("${db.password}") String password,
        @Value("${db.url}") String url,
        @Value("${db.schema}") String defaultSchema
    ) {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", driverClass);
        properties.setProperty("hibernate.connection.url", url);
        properties.setProperty("hibernate.connection.username", username);
        properties.setProperty("hibernate.connection.password", password);
        properties.setProperty("hibernate.default_schema", defaultSchema);
        return properties;
    }
}
