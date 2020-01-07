package com.trzewik.spring.interfaces.rest

import com.trzewik.spring.domain.service.GameService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import spock.mock.DetachedMockFactory

@Profile('test-rest')
@Import(RestConfiguration)
@Configuration
class TestRestConfig {
    DetachedMockFactory factory = new DetachedMockFactory()

    GameService gameServiceMock(){
        return factory.Mock(GameService)
    }
}
