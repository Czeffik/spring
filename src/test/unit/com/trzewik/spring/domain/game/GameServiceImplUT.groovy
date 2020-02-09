package com.trzewik.spring.domain.game


import spock.lang.Specification
import spock.lang.Subject

class GameServiceImplUT extends Specification implements GameCreation {

    GameRepository gameRepo = new GameRepositoryMock()

    @Subject
    GameService service = GameServiceFactory.create(gameRepo)

    def 'should create game with croupier and deck and save in repositories'() {
        given:
        def croupierId = '12312312312'

        when:
        Game savedGame = service.create(croupierId)

        then:
        gameRepo.repository.size() == 1
        with(gameRepo.findById(savedGame.id).get()) {
            it == savedGame
            getCroupierId() == croupierId
            getCroupier()
            with(getDeck()) {
                getCards() != createCards()
                getCards().size() == 52
            }
            with(getPlayers()) {
                size() == 1
                first().playerId == croupierId
            }

        }
    }

    def 'should add new player to game with gameId and update game in repository'() {
        given:
        Game game = service.create('12341')

        and:
        def playerId = '12309'

        when:
        Game after = service.addPlayer(game.id, playerId)

        then:
        with(gameRepo.repository) {
            size() == 1
            with(get(game.id)) {
                it == after
                players.size() == 2
                players.any { it.playerId == playerId }
            }
        }
    }

    def 'should NOT add player to game and throw exception when can not find game with gameId'() {
        when:
        service.addPlayer('wrong-game-id', 'player-id')

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'

        and:
        gameRepo.repository.isEmpty()
    }

    def 'should NOT add player to game and throw exception when trying add player to game which is started'() {
        given:
        Game game = service.create('croupier-id')
        service.addPlayer(game.id, 'player-id')
        service.start(game.id)

        when:
        service.addPlayer(game.id, 'player-id-2')

        then:
        thrown(GameException)

        and:
        with(gameRepo.repository) {
            size() == 1
            with(get(game.id)) {
                players.size() == 2
                !players.any { it.playerId == 'player-id-2' }
            }
        }
    }

    def '''should find game by id in repository and start it by
        dealing cards, setting currentPlayer and set game status to STARTED'''() {
        given:
        Game game = service.create('croupier-id')
        service.addPlayer(game.id, 'player-id')

        when:
        def returned = service.start(game.id)

        then:
        with(gameRepo.findById(game.id).get()) {
            status == Game.Status.STARTED
            deck.cards.size() == 48
            currentPlayerId == 'player-id'
        }
        returned
    }

    def 'should NOT start game and throw exception when can not find game with gameId'() {
        when:
        service.start('wrong-game-id')

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'
    }

    def 'should NOT start game when no players added to game and throw exception'() {
        given:
        Game game = service.create('croupier-id')

        when:
        service.start(game.id)

        then:
        thrown(GameException)

        and:
        game.status == Game.Status.NOT_STARTED

        and:
        gameRepo.findById(game.id).get().status == Game.Status.NOT_STARTED
    }

    def 'should throw exception when trying start game which is already started'() {
        given:
        Game game = service.create('croupier-id')
        service.addPlayer(game.id, 'player-id')
        service.start(game.id)

        when:
        service.start(game.id)

        then:
        thrown(GameException)
    }

    def '''should find game in repository
        and player should make move HIT (draw card from deck)
        and game should be updated in repository'''() {
        given:
        Game game = service.create('croupier-id')
        def playerId = 'player-id'
        service.addPlayer(game.id, playerId)
        service.start(game.id)

        when:
        Game returnedGame = service.makeMove(game.id, playerId, Game.Move.HIT)

        then:
        with(gameRepo.findById(game.id).get()) {
            deck.cards.size() <= 47        //less because can have more than 21 points and it will finish the game
            with(players.find { it.playerId == playerId }) {
                hand.size() == 3
                move == Game.Move.HIT
            }
        }

        and:
        returnedGame
    }

    def 'should throw exception when can not find game with gameId in game repository when trying make move'() {
        given:
        Game game = service.create('croupier-id')
        service.addPlayer(game.id, 'player-id')
        service.start(game.id)

        when:
        service.makeMove('wrong-game-id', 'player-id', Game.Move.HIT)

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'
    }

    def 'should not be possible to make move when game was not started'() {
        given:
        Game game = service.create('croupier-id')
        service.addPlayer(game.id, 'player-id')

        when:
        service.makeMove(game.id, 'player-id', Game.Move.HIT)

        then:
        thrown(GameException)
    }

    def 'should end game when last player move was STAND and should not be possible to make next move'() {
        given:
        Game game = service.create('croupier-id')
        def playerId = 'player-id'
        service.addPlayer(game.id, playerId)
        service.start(game.id)

        when:
        service.makeMove(game.id, playerId, Game.Move.STAND)

        then:
        gameRepo.findById(game.id).get().status == Game.Status.ENDED
        with(gameRepo.findById(game.id).get()) {
            with(players.find { it.playerId == playerId }) {
                hand.size() == 2
                move == Game.Move.STAND
            }
        }

        when:
        service.makeMove(game.id, playerId, Game.Move.HIT)

        then:
        thrown(GameException)
    }

    def 'should find game in repository and return game results when game is ended'() {
        given:
        Game game = service.create('croupier-id')
        service.addPlayer(game.id, 'player-id')
        service.start(game.id)
        service.makeMove(game.id, 'player-id', Game.Move.STAND)

        when:
        List<Result> results = service.getResults(game.id)

        then:
        results.size() == 2
    }

    def 'should throw exception when can not find game in repository with gameId when getting results'() {
        when:
        service.getResults('wrong-game-id')

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'
    }

    def 'should not be possible get results from game which is not ended'() {
        given:
        Game game = service.create('croupier-id')
        service.addPlayer(game.id, 'player-id')
        service.start(game.id)

        when:
        service.getResults(game.id)

        then:
        thrown(GameException)
    }
}