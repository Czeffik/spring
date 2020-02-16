package com.trzewik.spring.domain.game

import spock.lang.Specification

class GameFactoryUT extends Specification implements GamePlayerCreation, DeckCreation {

    def 'should create game with id, one player(croupier), croupier, deck, status set to NOT_STARTED and currentPlayer null'() {
        given:
        def croupier = createPlayer()

        when:
        def game = GameFactory.createGame(croupier)

        then:
        game.id
        game.players.size() == 1
        with(game.players.first()) {
            it.player.id == croupier.id
            it.player.name == croupier.name
            hand.isEmpty()
            move == Game.Move.HIT
        }
        with(game.deck) {
            it
            it.cards.size() == 52
        }
        game.status == Game.Status.NOT_STARTED
        game.currentPlayerId == null
    }

    def 'should create game with given: id, players, croupier, deck, status, currentPlayer'() {
        given:
        def id = '123'
        def players = createGamePlayers(3)
        def croupierId = players.first().player.id
        def deck = createDeck()
        def status = Game.Status.STARTED
        def currentPlayerId = players[1].player.id

        when:
        def game = GameFactory.createGame(id, players, croupierId, deck, status, currentPlayerId)

        then:
        game.id == id
        game.players == players
        game.croupierId == croupierId
        game.deck == deck
        game.status == status
        game.currentPlayerId == currentPlayerId
    }

    def 'should throw exception when id is null'() {
        when:
        GameFactory.createGame(null, createGamePlayers(2), '', createDeck(), Game.Status.STARTED, '')

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when players are null'() {
        when:
        GameFactory.createGame('', null, '', createDeck(), Game.Status.STARTED, '')

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when croupier is null'() {
        when:
        GameFactory.createGame('', createGamePlayers(), null, createDeck(), Game.Status.STARTED, '')

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when deck is null'() {
        when:
        GameFactory.createGame('', createGamePlayers(), '', null, Game.Status.STARTED, '')

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when status is null'() {
        when:
        GameFactory.createGame('', createGamePlayers(), '', createDeck(), null, '')

        then:
        thrown(NullPointerException)
    }
}
