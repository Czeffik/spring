package com.trzewik.spring.domain.game


import spock.lang.Specification

class ResultUT extends Specification implements GamePlayerCreation {

    def 'should create results'() {
        given:
        int place = 3
        def player = createGamePlayer()

        when:
        Result result = new Result(place, player)

        then:
        result.getPlace() == place
        result.getPlayer() == player
    }
}
