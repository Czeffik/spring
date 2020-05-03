package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.player.PlayerCreation
import spock.lang.Specification

class ResultUT extends Specification implements PlayerCreation, PlayerInGameCreation {

    def 'should create results'() {
        given:
        def place = 3
        def player = createPlayer()
        def playerInGame = createPlayerInGame()

        when:
        def result = new Result(place, player, playerInGame)

        then:
        result.place == place
        result.name == player.name
        result.hand == playerInGame.hand
        result.handValue == playerInGame.handValue()
    }

    def 'should throw exception when creating result with player as null'() {
        when:
        new Result(3, null, createPlayerInGame())
        then:
        NullPointerException ex = thrown()
        ex.message == 'player is marked non-null but is null'
    }

    def 'should throw exception when creating result with player in game as null'() {
        when:
        new Result(3, createPlayer(), null)
        then:
        NullPointerException ex = thrown()
        ex.message == 'playerInGame is marked non-null but is null'
    }
}
