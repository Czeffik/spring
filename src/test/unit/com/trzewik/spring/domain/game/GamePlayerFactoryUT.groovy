package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.player.PlayerCreation
import spock.lang.Specification

class GamePlayerFactoryUT extends Specification implements PlayerCreation {

    def player = createPlayer()

    def 'should create game player with given player'() {
        when:
        def gamePlayer = GamePlayerFactory.create(player)

        then:
        gamePlayer.id == player.id
        gamePlayer.name == player.name
        gamePlayer.hand.isEmpty()
        gamePlayer.move == Move.HIT
    }

    def 'should create game player with given player hand and move'() {
        given:
        def hand = [] as Set
        def move = Move.STAND

        when:
        def gamePlayer = GamePlayerFactory.create(player, hand, move)

        then:
        gamePlayer.id == player.id
        gamePlayer.name == player.name
        gamePlayer.hand == hand
        gamePlayer.move == move
    }

    def 'should throw exception when trying create game player with null player'() {
        when:
        GamePlayerFactory.create(null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when trying create game player with null hand'() {
        when:
        GamePlayerFactory.create(player, null, Move.HIT)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when trying create game player with null move'() {
        when:
        GamePlayerFactory.create(player, [] as Set, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when trying create game player with null player(with hand and move)'() {
        when:
        GamePlayerFactory.create(null, [] as Set, Move.STAND)

        then:
        thrown(NullPointerException)
    }
}
