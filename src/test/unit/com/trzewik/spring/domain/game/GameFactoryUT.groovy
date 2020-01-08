package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.deck.DeckCreation
import com.trzewik.spring.domain.player.PlayerCreation
import spock.lang.Specification

class GameFactoryUT extends Specification implements PlayerCreation, DeckCreation {

    def 'should create game with id, one player(croupier), croupier, deck, status set to NOT_STARTED and currentPlayer null'() {
        when:
        Game game = GameFactory.createGame()

        then:
        game.@id != null
        game.@players.size() == 1
        with(game.@croupier) {
            it != null
            it == game.@players.first()
            name == 'Croupier'
        }
        with(game.@deck) {
            it != null
            it.@cards.size() == 52
        }
        game.@status == Game.Status.NOT_STARTED
        game.@currentPlayer == null
    }

    def 'should create game with given: id, players, croupier, deck, status, currentPlayer'() {
        given:
        def id = '123'
        def players = createPlayers(2)
        def croupier = createPlayer(new PlayerBuilder(name: 'Croupier'))
        def deck = createDeck()
        def status = Game.Status.STARTED
        def currentPlayer = players[1]

        when:
        def game = GameFactory.createGame(id, players, croupier, deck, status, currentPlayer)

        then:
        game.@id == id
        game.@players == players
        game.@croupier == croupier
        game.@deck == deck
        game.@status == status
        game.@currentPlayer == currentPlayer
    }

    def 'should throw exception when id is null'() {
        when:
        GameFactory.createGame(null, createPlayers(2), createPlayer(), createDeck(), Game.Status.STARTED, createPlayer())

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when players are null'() {
        when:
        GameFactory.createGame('', null, createPlayer(), createDeck(), Game.Status.STARTED, createPlayer())

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when croupier is null'() {
        when:
        GameFactory.createGame('', createPlayers(2), null, createDeck(), Game.Status.STARTED, createPlayer())

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when deck is null'() {
        when:
        GameFactory.createGame('', createPlayers(2), createPlayer(), null, Game.Status.STARTED, createPlayer())

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when status is null'() {
        when:
        GameFactory.createGame('', createPlayers(2), createPlayer(), createDeck(), null, createPlayer())

        then:
        thrown(NullPointerException)
    }
}
