package com.trzewik.spring.domain.player

import spock.lang.Specification

class PlayerServiceFactoryUT extends Specification {

    def 'should create game service with given repositories'() {
        given:
            def repository = Mock(PlayerRepository)

        when:
            def service = PlayerServiceFactory.create(repository)

        then:
            service.@playerRepo == repository
    }
}
