package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerCreation
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class GameImplUT extends Specification implements PlayerCreation {

    @Subject
    Game game = GameFactory.createGame()

    def 'should create game without players, with deck and croupier'() {
        expect:
        game.@players.size() == 0
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
        game.@players.size() == 1
    }

    def 'should be possible get game id'() {
        expect:
        game.getId() == game.@id
    }

    def 'should be possible get croupier'() {
        expect:
        game.getCroupier() == game.@croupier
    }

    def 'should be possible get current player - when is null'() {
        expect:
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

    def 'should throw exception when trying start game without players'() {
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
        game.@currentPlayer == players.first()

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
        game.auction(createPlayer(), Game.Move.HIT)

        then:
        GameException ex = thrown()
        ex.message == 'Game NOT started, please start game before auction'
    }

    def 'should throw exception when doing auction when game ended'() {
        given:
        game.@status = Game.Status.ENDED

        when:
        game.auction(createPlayer(), Game.Move.HIT)

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
        game.auction(players[1], Game.Move.HIT)

        then:
        GameException ex = thrown()
        ex.message == "Waiting for move from player: [${players.first().id}] instead of: [${players.get(1).id}]"
    }

    def 'should throw exception when player which stand trying make move'() {
        given:
        def players = createPlayers()

        and:
        setupGame(players)

        and:
        players.first().@move = Game.Move.STAND

        when:
        game.auction(players.first(), Game.Move.HIT)

        then:
        GameException ex = thrown()
        ex.message == "Player [${players.first().getId()}] can not make move. He stands."
    }

    def 'should make HIT move - setPlayer move to HIT, get extra card from deck'() {
        given:
        def players = createPlayers()

        and:
        setupGame(players)

        when:
        Game gameAfterAuction = game.auction(players.first(), Game.Move.HIT)

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
        Game gameAfterAuction = game.auction(players.first(), Game.Move.STAND)

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
        game.auction(players.first(), Game.Move.STAND)

        and:
        game.auction(players[1], Game.Move.STAND)

        then:
        players.first().hand.size() == 2
        players.first().move == Game.Move.STAND

        and:
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
            game.auction(player, Game.Move.HIT)
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
        players.each { game.auction(it, Game.Move.STAND) }

        when:
        def results = game.getResults()

        then:
        results.size() == 3
        results.collect { it.@player }.containsAll(players)
    }

    Game setupGame(List<Player> players) {
        players.each {
            game.addPlayer(it)
        }
        return game.startGame()
    }
}
