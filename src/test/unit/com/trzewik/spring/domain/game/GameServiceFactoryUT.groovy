package com.trzewik.spring.domain.game

import spock.lang.Specification

class GameServiceFactoryUT extends Specification {
    def 'should create game service with given repositories'() {
        given:
        def game = Mock(GameRepository)

        when:
        def service = GameServiceFactory.create(game)

        then:
        service.@gameRepo == game
    }
}
