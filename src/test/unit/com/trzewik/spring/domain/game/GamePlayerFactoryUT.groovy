package com.trzewik.spring.domain.game

import spock.lang.Specification

class GamePlayerFactoryUT extends Specification {

    def 'should create game player with id'() {
        given:
        def playerId = 'player-id'

        when:
        def player = GamePlayerFactory.create(playerId)

        then:
        player.playerId == playerId
        player.hand.isEmpty()
        player.move == Game.Move.HIT
    }

    def 'should create game player with player id, hand and move'() {
        given:
        def playerId = 'player-id'
        def hand = [] as Set
        def move = Game.Move.STAND

        when:
        def player = GamePlayerFactory.create(playerId, hand, move)

        then:
        player.playerId == playerId
        player.hand == hand
        player.move == move
    }

    def 'should throw exception when trying create game player with null playerId'() {
        when:
        GamePlayerFactory.create(null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when trying create game player with null hand'() {
        when:
        GamePlayerFactory.create('', null, Game.Move.HIT)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when trying create game player with null move'() {
        when:
        GamePlayerFactory.create('', [] as Set, null)

        then:
        thrown(NullPointerException)
    }
}
