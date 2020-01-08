package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.common.Deck
import com.trzewik.spring.domain.deck.DeckCreation
import com.trzewik.spring.domain.common.DeckFactory
import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerCreation
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class GameImplUT extends Specification implements PlayerCreation, DeckCreation {

    @Subject
    Game game = GameFactory.createGame()

    def 'should create game with one player, with deck and croupier'() {
        expect:
        game.@players.size() == 1
        game.@deck != null
        game.@deck.cards.size() == 52
        game.@croupier != null
        game.@croupier.@name == 'Croupier'
        game.@status == Game.Status.NOT_STARTED
        game.@currentPlayer == null
    }

    def 'should be possible add player to game'() {
        when:
        game.addPlayer(createPlayer())

        then:
        game.@players.size() == 2
    }

    def 'should be possible get game id'() {
        expect:
        game.getId() == game.@id
    }

    def 'should be possible get croupier'() {
        expect:
        game.getCroupier() == game.@croupier
    }

    def 'should be possible get croupier id'() {
        expect:
        game.getCroupierId() == game.@croupier.id
    }

    def 'should be possible get current player - when is null'() {
        expect:
        game.getCurrentPlayer() == null
        game.getCurrentPlayer() == game.@currentPlayer
    }

    def 'should be possible get current player - when is not null'() {
        given:
        Player player = createPlayer()
        game.addPlayer(player)
        game.startGame()

        expect:
        game.getCurrentPlayer() == player
    }

    def 'should be possible get current player id - when current player is null'() {
        expect:
        game.getCurrentPlayerId() == null
    }

    def 'should be possible get current player id - when current player is not null'() {
        given:
        Player player = createPlayer()
        game.addPlayer(player)
        game.startGame()

        expect:
        game.getCurrentPlayerId() == player.id
    }

    @Unroll
    def 'should throw exception when trying add player when game was #STATUS'() {
        given:
        game.@status = STATUS

        when:
        game.addPlayer(createPlayer())

        then:
        GameException ex = thrown()
        ex.message == "Game started, can not add new player"

        where:
        STATUS << [Game.Status.STARTED, Game.Status.ENDED]
    }

    @Unroll
    def 'should throw exception when trying start already started game #STATUS'() {
        given:
        game.@status = STATUS

        when:
        game.startGame()

        then:
        GameException ex = thrown()
        ex.message == "Game started, can not start again"

        where:
        STATUS << [Game.Status.STARTED, Game.Status.ENDED]
    }

    def 'should throw exception when trying start game without players (only with croupier)'() {
        when:
        game.startGame()

        then:
        GameException ex = thrown()
        ex.message == "Please add at least one player before start."
    }

    def 'should start game successfully  when at least one player is added - dealCards, set status to started and set currentPlayer'() {
        given:
        def players = createPlayers()

        when:
        Game startedGame = setupGame(players)

        then:
        game.@croupier.getHand().size() == 2
        game.@players.each {
            assert it.getHand().size() == 2
        }
        game.@deck.@cards.size() == 52 - 6

        and:
        game.@status == Game.Status.STARTED

        and:
        game.@currentPlayer != null

        and:
        startedGame.is(game)
    }

    @Unroll
    def 'should get game status: #STATUS'() {
        given:
        game.@status = STATUS

        expect:
        game.getStatus() == STATUS

        where:
        STATUS << Game.Status.values()
    }

    def 'should throw exception when doing auction without starting game'() {
        when:
        game.auction(createPlayer().id, Game.Move.HIT)

        then:
        GameException ex = thrown()
        ex.message == 'Game NOT started, please start game before auction'
    }

    def 'should throw exception when doing auction when game ended'() {
        given:
        game.@status = Game.Status.ENDED

        when:
        game.auction(createPlayer().id, Game.Move.HIT)

        then:
        GameException ex = thrown()
        ex.message == 'Game finished. Now you can check results!'
    }

    def 'should throw exception when wrong player (not this player turn) trying auction'() {
        given:
        def players = createPlayers()

        and:
        setupGame(players)

        when:
        game.auction(players[1].id, Game.Move.HIT)

        then:
        GameException ex = thrown()
        ex.message == "Waiting for move from player: [${players.first().id}] instead of: [${players.get(1).id}]"
    }

    def 'should make HIT move - setPlayer move to HIT, get extra card from deck'() {
        given:
        def players = createPlayers()

        and:
        setupGame(players)

        when:
        Game gameAfterAuction = game.auction(players.first().id, Game.Move.HIT)

        then:
        players.first().hand.size() == 3
        players.first().move == Game.Move.HIT

        and:
        game.@deck.@cards.size() == 52 - 6 - 1

        and:
        game.status == Game.Status.STARTED

        and:
        gameAfterAuction.is(game)
    }

    def 'should make STAND move - setPlayer move to STAND'() {
        given:
        def players = createPlayers()

        and:
        setupGame(players)

        when:
        Game gameAfterAuction = game.auction(players.first().id, Game.Move.STAND)

        then:
        players.first().hand.size() == 2
        players.first().move == Game.Move.STAND

        and:
        game.@deck.@cards.size() == 52 - 6

        and:
        game.@currentPlayer == players[1]

        and:
        game.status == Game.Status.STARTED

        and:
        gameAfterAuction.is(game)
    }

    def 'should end game when all players make stand move'() {
        given:
        def players = createPlayers()

        and:
        setupGame(players)

        when:
        game.auction(players.first().id, Game.Move.STAND)

        then:
        players.first().hand.size() == 2
        players.first().move == Game.Move.STAND

        and:
        game.@currentPlayer == players[1]

        when:
        game.auction(players[1].id, Game.Move.STAND)

        then:
        players.get(1).hand.size() == 2
        players.get(1).move == Game.Move.STAND

        and:
        game.@croupier.handValue() >= 17

        and:
        game.@currentPlayer == null

        and:
        game.status == Game.Status.ENDED
    }

    def 'should end player turn when has more than 21 point on hand'() {
        given:
        def player = createPlayer()

        and:
        setupGame([player])

        when:
        while (player.handValue() <= 21) {
            game.auction(player.id, Game.Move.HIT)
        }

        then:
        player.isLooser()

        and:
        game.@croupier.handValue() >= 17

        and:
        game.@currentPlayer == null

        and:
        game.status == Game.Status.ENDED
    }

    def 'should throw exception when trying get results when game was not ended'() {
        when:
        game.getResults()

        then:
        GameException ex = thrown()
        ex.message == 'Results are available only when game finished. Please continue auction.'
    }

    def 'should return results when game ended'() {
        given:
        def players = createPlayers()

        and:
        setupGame(players)

        and:
        players.each { game.auction(it.id, Game.Move.STAND) }

        when:
        def results = game.getResults()

        then:
        results.size() == 3
        results.collect { it.@player }.containsAll(players)
    }

    def 'should return list with one player (croupier) when no players added'() {
        expect:
        game.getPlayers().size() == 1
    }

    def 'should return list with players'() {
        given:
        Player player = createPlayer()

        and:
        game.addPlayer(player)

        when:
        def players = game.getPlayers()

        then:
        players.size() == 2
        players.contains(player)
    }

    def 'should return deck with all cards'() {
        expect:
        game.getDeck().@cards.size() == 52
    }

    def 'should return deck without cards which was picked'() {
        given:
        3.times {
            game.@deck.take()
        }

        expect:
        game.getDeck().@cards.size() == 49
    }

    def 'should throw exception when id is null'() {
        when:
        new GameImpl(null, [], createPlayer(), DeckFactory.createDeck(), Game.Status.NOT_STARTED, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when players are null'() {
        when:
        new GameImpl('', null, createPlayer(), DeckFactory.createDeck(), Game.Status.NOT_STARTED, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when croupier is null'() {
        when:
        new GameImpl('', [], null, DeckFactory.createDeck(), Game.Status.NOT_STARTED, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when deck is null'() {
        when:
        new GameImpl('', [], createPlayer(), null, Game.Status.NOT_STARTED, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when status is null'() {
        when:
        new GameImpl('', [], createPlayer(), DeckFactory.createDeck(), null, null)

        then:
        thrown(NullPointerException)
    }

    def 'should shuffle deck on game start'() {
        given:
        Deck deck = Mock()
        def game = new GameImpl('12312', createPlayers(), createPlayer(), deck, Game.Status.NOT_STARTED, createPlayer())

        when:
        game.startGame()

        then:
        1 * deck.shuffle()
        deck.take() >> createCard()
    }

    def 'croupier should pick cards only if his hand value is lesser than 17'() {
        given:
        Player croupier = Mock()
        def currentPlayer = createPlayer()
        def players = [croupier,  currentPlayer]
        def game = new GameImpl('12312', players, croupier, createDeck(), Game.Status.STARTED, currentPlayer)

        when:
        game.auction(currentPlayer.id, Game.Move.STAND)

        then:
        1 * croupier.handValue() >> 17
        0 * croupier.addCard(_)
    }

    def 'croupier should pick cards till his hand has value higher than 16'() {
        given:
        Player croupier = Mock()
        def currentPlayer = createPlayer()
        def players = [croupier,  currentPlayer]
        def game = new GameImpl('12312', players, croupier, createDeck(), Game.Status.STARTED, currentPlayer)

        when:
        game.auction(currentPlayer.id, Game.Move.STAND)

        then:
        1 * croupier.handValue() >> 16
        1 * croupier.addCard(_)
        1 * croupier.handValue() >> 25
    }

    Game setupGame(List<Player> players) {
        players.each {
            game.addPlayer(it)
        }
        return game.startGame()
    }
}
