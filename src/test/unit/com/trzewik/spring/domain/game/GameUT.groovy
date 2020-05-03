package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerCreation
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class GameUT extends Specification implements PlayerCreation, PlayerInGameCreation, DeckCreation, CardCreation, GameCreation {

    def croupier = createPlayer(PlayerCreator.croupier())

    @Subject
    def game = new Game(croupier)

    def 'should end game when all players made moves and game was started'() {
        given:
        def game = createGame(new GameCreator(
            status: Game.Status.STARTED,
            players: [(createPlayer(PlayerCreator.croupier())): createPlayerInGame(),
                      (createPlayer())                        : createPlayerInGame(new PlayerInGameCreator(move: Game.Move.STAND))]
        ))
        when:
        def endedGame = game.end()
        then:
        game != endedGame
        and:
        endedGame.status == Game.Status.ENDED
        with(endedGame.players.get(croupier)) {
            move == Game.Move.STAND
            handValue() >= 17
        }
    }

    def 'croupier should draw cards only when his hand value is lesser than 17'() {
        given:
        def croupier = Mock(PlayerInGame)
        and:
        def game = createGame(new GameCreator(
            status: Game.Status.STARTED,
            players: [(createPlayer(PlayerCreator.croupier())): croupier,
                      (createPlayer())                        : createPlayerInGame(new PlayerInGameCreator(move: Game.Move.STAND))]
        ))
        when:
        game.end()
        then:
        1 * croupier.handValue() >> 17
        1 * croupier.changeMove(Game.Move.STAND) >> createPlayerInGame(new PlayerInGameCreator(move: Game.Move.STAND))
        0 * croupier.addCard(_)
    }

    def 'croupier should draw cards until his hand has value greater than 16'() {
        given:
        def croupier = Mock(PlayerInGame)
        and:
        def game = createGame(new GameCreator(
            status: Game.Status.STARTED,
            players: [(createPlayer(PlayerCreator.croupier())): croupier,
                      (createPlayer())                        : createPlayerInGame(new PlayerInGameCreator(move: Game.Move.STAND))]
        ))
        when:
        game.end()
        then:
        1 * croupier.handValue() >> 16
        1 * croupier.addCard(_) >> croupier
        1 * croupier.handValue() >> 17
        1 * croupier.changeMove(Game.Move.STAND) >> createPlayerInGame(new PlayerInGameCreator(move: Game.Move.STAND))
    }

    def 'should do nothing when current game has active player'() {
        given:
        def game = createGame(new GameCreator(
            status: Game.Status.STARTED,
            players: [(createPlayer(PlayerCreator.croupier())): createPlayerInGame(),
                      (createPlayer())                        : createPlayerInGame(new PlayerInGameCreator(move: Game.Move.HIT))]
        ))
        when:
        def endedGame = game.end()
        then:
        game == endedGame
    }

    @Unroll
    def 'should do nothing when current game has status: #STATUS'() {
        given:
        def game = createGame(new GameCreator(
            status: STATUS
        ))
        when:
        def endedGame = game.end()
        then:
        game == endedGame
        where:
        STATUS                  | _
        Game.Status.NOT_STARTED | _
        Game.Status.ENDED       | _
    }

    def 'should make HIT move for player with given id'() {
        given:
        def game = createGame(new GameCreator(status: Game.Status.STARTED))
        def playerId = game.players.keySet().find { it.id != game.croupier.id }.id
        when:
        def gameAfterAuction = game.auction(playerId, Game.Move.HIT)
        then:
        game != gameAfterAuction
        and:
        gameAfterAuction.status == Game.Status.STARTED
        with(gameAfterAuction.players.find { it.key.id == playerId }.value) {
            hand.size() == 1
            move == Game.Move.HIT
        }
    }

    def 'should make STAND move for player with given id'() {
        given:
        def game = createGame(new GameCreator(status: Game.Status.STARTED))
        def playerId = game.players.keySet().find { it.id != game.croupier.id }.id
        when:
        def gameAfterAuction = game.auction(playerId, Game.Move.STAND)
        then:
        game != gameAfterAuction
        and:
        gameAfterAuction.status == Game.Status.STARTED
        with(gameAfterAuction.players.find { it.key.id == playerId }.value) {
            hand.size() == 0
            move == Game.Move.STAND
        }
    }

    def 'should throw exception when trying make auction in not started game'() {
        given:
        def game = createGame()
        def playerId = game.players.keySet().find { it.id != game.croupier.id }.id
        when:
        game.auction(playerId, Game.Move.HIT)
        then:
        Game.Exception ex = thrown()
        ex.message == 'Game NOT started, please start game before auction'
    }

    def 'should throw exception when trying make auction in ended game'() {
        given:
        def game = createGame(new GameCreator(status: Game.Status.ENDED))
        def playerId = game.players.keySet().find { it.id != game.croupier.id }.id
        when:
        game.auction(playerId, Game.Move.HIT)
        then:
        Game.Exception ex = thrown()
        ex.message == 'Game finished. Now you can check results!'
    }

    def 'should throw exception when other player try make auction (not current player)'() {
        given:
        def game = createGame(new GameCreator(status: Game.Status.STARTED))
        def playerId = game.players.keySet().find { it.id != game.croupier.id }.id
        def otherId = 'other-player-id'
        when:
        game.auction(otherId, Game.Move.HIT)
        then:
        Game.Exception ex = thrown()
        ex.message == "Waiting for move from player: [${playerId}] instead of: [${otherId}]"
    }

    def 'should start game'() {
        given:
        def game = createGame()
        when:
        def startedGame = game.start()
        then:
        game != startedGame
        and:
        startedGame.status == Game.Status.STARTED
        and:
        startedGame.players.values().each {
            assert it.hand.size() == 2
        }
        and:
        startedGame.deck.cards.size() == 52 - (2 * game.players.size())
    }

    def 'should throw exception when starting already started game'() {
        given:
        def game = createGame(new GameCreator(status: Game.Status.STARTED))
        when:
        game.start()
        then:
        Game.Exception ex = thrown()
        ex.message == 'Game started, can not start again'
    }

    def 'should throw exception when starting game without players'() {
        when:
        game.start()
        then:
        Game.Exception ex = thrown()
        ex.message == 'Please add at least one player before start.'
    }

    def 'should add player to game'() {
        given:
        def player = createPlayer()
        when:
        def gameWithPlayer = game.addPlayer(player)
        then:
        game != gameWithPlayer
        and:
        gameWithPlayer.players.size() == 2
        gameWithPlayer.players.keySet().contains(player)
    }

    def 'should throw exception when adding player to started game'() {
        given:
        def player = createPlayer()
        and:
        def game = createGame(new GameCreator(status: Game.Status.STARTED))
        when:
        game.addPlayer(player)
        then:
        Game.Exception ex = thrown()
        ex.message == 'Game started, can not add new player'
    }

    def 'should throw exception when adding croupier to game'() {
        given:
        def game = createGame()
        when:
        game.addPlayer(croupier)
        then:
        Game.Exception ex = thrown()
        ex.message == "Player: [${croupier.toString()}] already added to game!"
    }

    def 'should create game with id, one player(croupier), croupier, deck, status set to NOT_STARTED'() {
        expect:
        game.id
        game.players.size() == 1
        game.players.keySet().first().is(croupier)
        game.deck
        game.croupier.is(croupier)
        game.status == Game.Status.NOT_STARTED
    }

    def 'should create game with given: id, deck, players, croupier and status'() {
        given:
        def id = '123'
        def deck = createDeck()
        def players = [:] as Map
        def status = Game.Status.STARTED

        when:
        def game = new Game(id, deck, players, croupier, status)

        then:
        game.id.is(id)
        game.deck.is(deck)
        game.players == players
        game.croupier.is(croupier)
        game.status.is(status)
    }

    def 'should throw exception when id is null'() {
        when:
        new Game(null, createDeck(), [:], croupier, Game.Status.NOT_STARTED)

        then:
        NullPointerException ex = thrown()
        ex.message == 'id is marked non-null but is null'
    }

    def 'should throw exception when deck is null'() {
        when:
        new Game('', null, [:], croupier, Game.Status.NOT_STARTED)

        then:
        NullPointerException ex = thrown()
        ex.message == 'deck is marked non-null but is null'
    }

    def 'should throw exception when players are null'() {
        when:
        new Game('', createDeck(), null, croupier, Game.Status.NOT_STARTED)

        then:
        NullPointerException ex = thrown()
        ex.message == 'players is marked non-null but is null'
    }

    def 'should throw exception when croupier is null'() {
        when:
        new Game('', createDeck(), [:], null, Game.Status.NOT_STARTED)

        then:
        NullPointerException ex = thrown()
        ex.message == 'croupier is marked non-null but is null'
    }

    def 'should throw exception when status is null'() {
        when:
        new Game('', createDeck(), [:], croupier, null)

        then:
        NullPointerException ex = thrown()
        ex.message == 'status is marked non-null but is null'
    }

    def 'should throw exception when croupier is null - one arg constructor'() {
        when:
        new Game(null as Player)

        then:
        NullPointerException ex = thrown()
        ex.message == 'null key in entry: null={hand=[], move=null}'
    }

    def 'should throw exception when trying add null player to game'() {
        when:
        game.addPlayer(null)

        then:
        NullPointerException ex = thrown()
        ex.message == 'player is marked non-null but is null'
    }

    def 'should throw exception when trying auction with null playerId'() {
        when:
        game.auction(null, Game.Move.HIT)

        then:
        NullPointerException ex = thrown()
        ex.message == 'playerId is marked non-null but is null'
    }

    def 'should throw exception when trying auction with null player move'() {
        when:
        game.auction('', null)

        then:
        NullPointerException ex = thrown()
        ex.message == 'move is marked non-null but is null'
    }
}
