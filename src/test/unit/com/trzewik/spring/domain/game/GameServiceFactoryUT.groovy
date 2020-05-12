package com.trzewik.spring.domain.game

import spock.lang.Specification

class GameServiceFactoryUT extends Specification {
    def 'should create game service with given repositories'() {
        expect:
            GameServiceFactory.create(Mock(GameRepository))
    }
}
