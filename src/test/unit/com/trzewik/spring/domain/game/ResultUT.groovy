package com.trzewik.spring.domain.game

import spock.lang.Specification

class ResultUT extends Specification implements PlayerInGameCreation {

    def 'should create results'() {
        given:
            def place = 3
            def playerInGame = createPlayerInGame()

        when:
            def result = new Result(place, playerInGame)

        then:
            result.place == place
            result.name == playerInGame.name
            result.hand == playerInGame.hand
            result.handValue == playerInGame.handValue()
    }

    def 'should throw exception when creating result with player in game as null'() {
        when:
            new Result(3, null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'playerInGame is marked non-null but is null'
    }
}
