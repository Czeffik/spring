package com.trzewik.spring.domain.game

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class GameImplUT extends Specification implements GamePlayerCreation, DeckCreation {

    @Subject
    Game game = GameFactory.createGame(createPlayer(new PlayerBuilder(id: 'croupier-id')))

    def 'should be possible add player to game'() {
        when:
        game.addPlayer(createPlayer())

        then:
        game.players.size() == 2
    }

    def 'should be possible get game id'() {
        expect:
        game.getId() == game.@id
    }

    def 'should be possible get croupier id'() {
        expect:
        game.getCroupierId() == game.@croupierId
    }


    def 'should be possible get current player id - when is null'() {
        expect:
        game.getCurrentPlayerId() == null
    }

    def 'should be possible get current player id - when current player is not null'() {
        given:
        def player = createPlayer()
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
        def players = createGamePlayers()

        when:
        Game startedGame = setupGame(players)

        then:
        game.players.each {
            assert it.getHand().size() == 2
        }
        game.deck.cards.size() == 52 - 6

        and:
        game.status == Game.Status.STARTED

        and:
        game.currentPlayer

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
        game.auction('player-id', Game.Move.HIT)

        then:
        GameException ex = thrown()
        ex.message == 'Game NOT started, please start game before auction'
    }

    def 'should throw exception when doing auction when game ended'() {
        given:
        game.@status = Game.Status.ENDED

        when:
        game.auction('player-id', Game.Move.HIT)

        then:
        GameException ex = thrown()
        ex.message == 'Game finished. Now you can check results!'
    }

    def 'should throw exception when wrong player (not this player turn) trying auction'() {
        given:
        def players = createGamePlayers()

        and:
        setupGame(players)

        when:
        game.auction(players[1].id, Game.Move.HIT)

        then:
        GameException ex = thrown()
        ex.message == "Waiting for move from player: [${players.first().id}] instead of: [${players[1].id}]".toString()
    }

    def 'should make HIT move - setPlayer move to HIT, get extra card from deck'() {
        given:
        def players = createGamePlayers()

        and:
        def firstPlayerId = players.first().id

        and:
        setupGame(players)

        when:
        Game gameAfterAuction = game.auction(firstPlayerId, Game.Move.HIT)

        then:
        with(game.players.find { it.id == firstPlayerId }) {
            hand.size() == 3
            move == Game.Move.HIT
        }

        and:
        game.deck.cards.size() == 52 - 6 - 1

        and:
        game.status == Game.Status.STARTED

        and:
        gameAfterAuction.is(game)
    }

    def 'should make STAND move - setPlayer move to STAND'() {
        given:
        def players = createGamePlayers()

        and:
        def firstPlayerId = players.first().id

        and:
        setupGame(players)

        when:
        Game gameAfterAuction = game.auction(firstPlayerId, Game.Move.STAND)

        then:
        with(game.players.find { it.id == firstPlayerId }) {
            hand.size() == 2
            move == Game.Move.STAND
        }

        and:
        game.deck.cards.size() == 52 - 6

        and:
        game.currentPlayer == players[1]

        and:
        game.status == Game.Status.STARTED

        and:
        gameAfterAuction.is(game)
    }

    def 'should end game when all players make stand move'() {
        given:
        def players = createGamePlayers()

        and:
        def firstPlayerId = players.first().id
        def secondPlayerId = players[1].id

        and:
        setupGame(players)

        when:
        game.auction(firstPlayerId, Game.Move.STAND)

        then:
        with(game.players.find { it.id == firstPlayerId }) {
            hand.size() == 2
            move == Game.Move.STAND
        }

        and:
        game.currentPlayer == players[1]

        when:
        game.auction(players[1].id, Game.Move.STAND)

        then:
        with(game.players.find { it.id == secondPlayerId }) {
            hand.size() == 2
            move == Game.Move.STAND
        }

        and:
        game.croupier.handValue() >= 17

        and:
        game.currentPlayer == null

        and:
        game.status == Game.Status.ENDED
    }

    def 'should end player turn when has more than 21 point on hand'() {
        given:
        def player = createGamePlayer()

        and:
        setupGame([player] as Set)

        when:
        while (game.players.find { it.id == player.id }.handValue() <= 21) {
            game.auction(player.id, Game.Move.HIT)
        }

        then:
        game.players.find { it.id == player.id }.isLooser()

        and:
        game.croupier.handValue() >= 17

        and:
        game.currentPlayer == null

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
        def players = createGamePlayers()

        and:
        setupGame(players)

        and:
        players.each { game.auction(it.id, Game.Move.STAND) }

        when:
        def results = game.getResults()

        then:
        results.size() == 3
        results.collect { it.player }.containsAll(players)
    }

    def 'should return list with one player (croupier) when no players added'() {
        expect:
        game.getPlayers().size() == 1
    }

    def 'should return list with players'() {
        given:
        def player = createGamePlayer()

        and:
        game.addPlayer(createPlayer(new PlayerBuilder(player)))

        when:
        def players = game.getPlayers()

        then:
        players.size() == 2
        players.contains(player)
    }

    def 'should return deck with all cards'() {
        expect:
        game.getDeck().cards.size() == 52
    }

    def 'should return deck without cards which was picked'() {
        given:
        3.times {
            game.deck.take()
        }

        expect:
        game.getDeck().cards.size() == 49
    }

    def 'should throw exception when id is null'() {
        when:
        new GameImpl(null, [] as Set, '', DeckFactory.createDeck(), Game.Status.NOT_STARTED, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when players are null'() {
        when:
        new GameImpl('', null, '', DeckFactory.createDeck(), Game.Status.NOT_STARTED, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when croupier id is null'() {
        when:
        new GameImpl('', [] as Set, null, DeckFactory.createDeck(), Game.Status.NOT_STARTED, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when deck is null'() {
        when:
        new GameImpl('', [] as Set, '', null, Game.Status.NOT_STARTED, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when status is null'() {
        when:
        new GameImpl('', [] as Set, '', DeckFactory.createDeck(), null, null)

        then:
        thrown(NullPointerException)
    }

    def 'croupier should pick cards only if his hand value is lesser than 17'() {
        given:
        def croupier = Mock(GamePlayer)
        def croupierId = 'croupier_id'
        def currentPlayer = createGamePlayer()
        def players = [croupier, currentPlayer] as Set
        def game = new GameImpl('12312', players, croupierId, createDeck(), Game.Status.STARTED, currentPlayer.id)
        croupier.id >> croupierId

        when:
        game.auction(currentPlayer.id, Game.Move.STAND)

        then:
        1 * croupier.handValue() >> 17
        0 * croupier.addCard(_)
    }

    def 'croupier should pick cards till his hand has value higher than 16'() {
        given:
        def croupier = Mock(GamePlayer)
        def croupierId = 'croupier_id'
        def currentPlayer = createGamePlayer()
        def players = [croupier, currentPlayer] as Set
        def game = new GameImpl('12312', players, croupierId, createDeck(), Game.Status.STARTED, currentPlayer.id)
        croupier.id >> croupierId

        when:
        game.auction(currentPlayer.id, Game.Move.STAND)

        then:
        1 * croupier.handValue() >> 16
        1 * croupier.addCard(_)
        1 * croupier.handValue() >> 25
    }

    def 'should be possible get croupier'() {
        given:
        setupGame(createGamePlayers())

        expect:
        with(game.getCroupier()) {
            it
            id == 'croupier-id'
        }
    }

    def 'should be possible get current player - when is not null'() {
        given:
        def player = createGamePlayer()
        game.addPlayer(createPlayer(new PlayerBuilder(player)))
        game.startGame()

        expect:
        game.getCurrentPlayer() == player
    }

    Game setupGame(Set<GamePlayer> players) {
        players.each {
            game.addPlayer(createPlayer(new PlayerBuilder(it)))
        }
        return game.startGame()
    }
}
