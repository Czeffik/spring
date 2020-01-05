package com.trzewik.spring.domain.player

import com.trzewik.spring.domain.deck.DeckCreation
import com.trzewik.spring.domain.game.Game
import spock.lang.Specification

class PlayerFactoryUT extends Specification implements DeckCreation {

    def 'should create player with given name, generated id, empty hand and Move.HIT'() {
        given:
        def name = 'Adam'

        when:
        def player = PlayerFactory.createPlayer(name)

        then:
        player.@name == name
        player.@id != null
        player.@hand.size() == 0
        player.@move == Game.Move.HIT
    }

    def 'should create croupier with generated id, empty hand and Move.HIT'() {
        when:
        def player = PlayerFactory.createCroupier()

        then:
        player.@name == 'Croupier'
        player.@id != null
        player.@hand.size() == 0
        player.@move == Game.Move.HIT
    }

    def 'should create player with given name and id, empty hand and move as null'() {
        given:
        def name = 'Adam'
        def id = '1231'

        when:
        def player = PlayerFactory.createPlayer(id, name)

        then:
        player.@name == name
        player.@id == id
        player.@hand.size() == 0
        player.@move == null
    }

    def 'should create player with given: name,id, hand and move'() {
        given:
        def name = 'Adam'
        def id = '1231'
        def hand = [createCard()] as Set
        def move = Game.Move.STAND

        when:
        def player = PlayerFactory.createPlayer(id, name, hand, move)

        then:
        player.@name == name
        player.@id == id
        player.@hand.size() == 1
        player.@hand == hand
        player.@move == move
    }

    def 'should throw exception when id is null'() {
        when:
        PlayerFactory.createPlayer(null, '', [] as Set, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when name is null'() {
        when:
        PlayerFactory.createPlayer('', null, [] as Set, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when hand is null'() {
        when:
        PlayerFactory.createPlayer('', '', null, null)

        then:
        thrown(NullPointerException)
    }
}
