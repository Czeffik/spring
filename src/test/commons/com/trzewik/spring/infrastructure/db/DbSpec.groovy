package com.trzewik.spring.infrastructure.db

import groovy.json.JsonSlurper
import org.flywaydb.core.Flyway
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.StandardEnvironment
import org.springframework.mock.env.MockPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Specification

abstract class DbSpec extends Specification {
    static final String DEFAULT_SCHEMA = 'test'
    private static PostgreSQLContainer container
    private static Flyway flyway
    protected static DbHelper helper
    protected JsonSlurper slurper = new JsonSlurper()

    def setupSpec() {
        startContainer()
        setupFlyway()
        migrateDb()
        helper = new DbHelper(container, DEFAULT_SCHEMA)
    }

    def setup(){
        clearDb()
    }

    def cleanup(){
        clearDb()
    }

    def cleanupSpec() {
        flyway.clean()
    }

    private static void startContainer() {
        if (container == null) {
            container = new PostgreSQLContainer()
            container.start()
        }
    }

    private static void clearDb(){
        helper.deletePlayerGames()
        helper.deleteGames()
        helper.deletePlayers()
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
                .schemas(DEFAULT_SCHEMA)
                .defaultSchema(DEFAULT_SCHEMA)
                .load()
        }
    }

    static class DbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        void initialize(ConfigurableApplicationContext applicationContext) {
            ConfigurableEnvironment env = applicationContext.environment
            MutablePropertySources propertySources = env.getPropertySources()
            MockPropertySource mock = new MockPropertySource()
                .withProperty('db.username', container.username)
                .withProperty('db.password', container.password)
                .withProperty('db.url', container.jdbcUrl)
                .withProperty('db.schema', DEFAULT_SCHEMA)
            propertySources.replace(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, mock)

        }
    }
}
