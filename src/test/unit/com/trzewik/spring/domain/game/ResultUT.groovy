package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.player.PlayerCreation
import spock.lang.Specification

class ResultUT extends Specification implements PlayerCreation {

    def 'should create results'() {
        given:
        int place = 3
        def player = createPlayer()

        when:
        Result result = new Result(place, player)

        then:
        result.getPlace() == place
        result.getPlayer() == player
    }
}
