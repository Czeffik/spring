package com.trzewik.spring.infrastructure.application

import com.trzewik.spring.domain.player.PlayerService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import spock.mock.DetachedMockFactory

@Profile(TestApplicationConfig.PROFILE)
@Import([ApplicationConfiguration.class])
@TestConfiguration
class TestApplicationConfig {
    public final static String PROFILE = 'test-application'
    DetachedMockFactory factory = new DetachedMockFactory()

    @Bean
    PlayerService playerServiceMock() {
        return factory.Mock(PlayerService)
    }
}
