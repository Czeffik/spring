package com.trzewik.spring.interfaces.rest

import com.trzewik.spring.domain.game.GameService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import spock.mock.DetachedMockFactory

@Profile('test-rest')
@Configuration
class TestRestConfig {
    DetachedMockFactory factory = new DetachedMockFactory()

    @Bean
    GameService gameServiceMock() {
        return factory.Mock(GameService)
    }
}
