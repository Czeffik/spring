package com.trzewik.spring.infrastructure.db;

import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.player.PlayerRepository;
import com.trzewik.spring.infrastructure.db.game.GameJpaRepository;
import com.trzewik.spring.infrastructure.db.game.GameRepositoryFactory;
import com.trzewik.spring.infrastructure.db.player.PlayerJpaRepository;
import com.trzewik.spring.infrastructure.db.player.PlayerRepositoryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
    "com.trzewik.spring.infrastructure.db.game",
    "com.trzewik.spring.infrastructure.db.player"
})
public class DbConfiguration {
    @Bean
    GameRepository gameRepository(GameJpaRepository gameJpaRepository) {
        return GameRepositoryFactory.create(gameJpaRepository);
    }

    @Bean
    PlayerRepository playerRepository(PlayerJpaRepository playerJpaRepository) {
        return PlayerRepositoryFactory.create(playerJpaRepository);
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        DataSource dataSource,
        Properties hibernateProperties
    ) {
        LocalContainerEntityManagerFactoryBean em
            = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(
            "com.trzewik.spring.infrastructure.db.game",
            "com.trzewik.spring.infrastructure.db.player"
        );

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties);

        return em;
    }

    @Bean
    public DataSource dataSource(
        @Value("${spring.datasource.driver-class-name}") String driverClass,
        @Value("${spring.datasource.username}") String username,
        @Value("${spring.datasource.password}") String password,
        @Value("${spring.datasource.url}") String url
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    Properties hibernateProperties(
        @Value("${spring.jpa.hibernate.ddl-auto}") String hbm2ddl,
        @Value("${spring.jpa.properties.hibernate.default_schema}") String defaultSchema
    ) {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);
        properties.setProperty("hibernate.default_schema", defaultSchema);
        return properties;
    }
}
