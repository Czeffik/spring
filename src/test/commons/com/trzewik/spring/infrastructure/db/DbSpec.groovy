package com.trzewik.spring.infrastructure.db

import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.StandardEnvironment
import org.springframework.mock.env.MockPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Shared
import spock.lang.Specification

abstract class DbSpec extends Specification {
    static final String defaultSchema = 'test'
    private static Flyway flyway
    protected static PostgreSQLContainer container
    @Shared
    Sql sql

    def setupSpec() {
        startContainer()
        setupFlyway()
        migrateDb()
        sql = Sql.newInstance(
            container.jdbcUrl,
            container.username,
            container.password
        )
    }

    def cleanupSpec() {
        flyway.clean()
        sql.close()
    }

    private static void startContainer() {
        if (container == null) {
            container = new PostgreSQLContainer()
            container.start()
        }
    }

    private static void migrateDb() {
        flyway.migrate()
    }

    private static void setupFlyway() {
        if (flyway == null) {
            flyway = Flyway.configure().dataSource(
                container.jdbcUrl,
                container.username,
                container.password)
                .schemas(defaultSchema)
                .defaultSchema(defaultSchema)
                .load()
        }
    }

    static class DbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        void initialize(ConfigurableApplicationContext applicationContext) {
            ConfigurableEnvironment env = applicationContext.environment
            MutablePropertySources propertySources = env.getPropertySources()
            PropertiesPropertySource mock = new MockPropertySource()
                .withProperty('spring.datasource.driver-class-name', container.driverClassName)
                .withProperty('spring.datasource.username', container.username)
                .withProperty('spring.datasource.password', container.password)
                .withProperty('spring.datasource.url', container.jdbcUrl)
                .withProperty('spring.jpa.properties.hibernate.default_schema', defaultSchema)
            propertySources.replace(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, mock)
        }
    }
}
