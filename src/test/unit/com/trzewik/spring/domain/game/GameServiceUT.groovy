package com.trzewik.spring.domain.game

import spock.lang.Specification
import spock.lang.Subject

class GameServiceUT extends Specification implements GameCreation, GameCommandCreation, PlayerInGameCreation {

    def repository = new GameRepositoryMock()
    def client = Mock(PlayerServiceClient)

    def croupier = createPlayerInGame(new PlayerInGameCreator(playerId: 'CROUPIER_ID', name: 'CROUPIER'))

    @Subject
    GameService service = GameServiceFactory.create(repository, client)

    def 'should create game with croupier and deck and save in repository'() {
        given:
            def command = createCreateGameCommand()
        when:
            def game = service.create(command)
        then:
            1 * client.getCroupier() >> this.croupier
        and:
            repository.repository.size() == 1
        and:
            repository.repository.get(game.id) == game
        and:
            with(game) {
                croupier == this.croupier
                with(deck) {
                    cards.size() == 52
                }
                with(players) {
                    size() == 1
                    with(first()) {
                        it == this.croupier
                        hand.isEmpty()
                        !move
                    }
                }
                status == Game.Status.NOT_STARTED
            }
    }

    def 'should add player to NOT_STARTED game and update game in repository'() {
        given:
            def game = putGameInRepo()
        and:
            def player = createPlayerInGame()
        and:
            def command = createAddPlayerToGameCommand(new AddPlayerToGameCommandCreator(game, player))
        when:
            def gameWithPlayer = service.addPlayer(command)
        then:
            1 * client.getPlayer(player.playerId) >> player
        and:
            repository.repository.size() == 1
        and:
            repository.repository.get(game.id) == gameWithPlayer
        and:
            with(gameWithPlayer) {
                players.size() == game.players.size() + 1

                def added = players.find { it.playerId == player.playerId }
                added == player
                with(added) {
                    hand.isEmpty()
                    !move
                }
            }
    }

    def 'should NOT add player to game and throw exception when can not find game with gameId'() {
        given:
            def game = putGameInRepo()
        and:
            def command = createAddPlayerToGameCommand()
        when:
            service.addPlayer(command)
        then:
            1 * client.getPlayer(command.playerId) >> createPlayerInGame()
        and:
            GameRepository.GameNotFoundException ex = thrown()
            ex.message == "Game with id: [$command.gameId] not found."
        and:
            repository.repository.size() == 1
        and:
            repository.repository.values().first() == game
    }

    def 'should NOT add player to game and throw exception when can not find player with playerId'() {
        given:
            def game = putGameInRepo()
        and:
            def command = createAddPlayerToGameCommand()
        and:
            def expectedMessage = "Can not find player with id: [${command.playerId}] in repository."
        when:
            service.addPlayer(command)
        then:
            1 * client.getPlayer(command.playerId) >> { throw new PlayerServiceClient.PlayerNotFoundException(new Exception(expectedMessage)) }
        and:
            PlayerServiceClient.PlayerNotFoundException ex = thrown()
            ex.message.contains(expectedMessage)
        and:
            repository.repository.size() == 1
        and:
            repository.repository.values().first() == game
    }

    def 'should NOT add player to game and throw exception when trying add player to game which is started'() {
        given:
            def game = putGameInRepo(new GameCreator(status: Game.Status.STARTED))
        and:
            def player = createPlayerInGame()
        and:
            def command = createAddPlayerToGameCommand(new AddPlayerToGameCommandCreator(game, player))
        when:
            service.addPlayer(command)
        then:
            1 * client.getPlayer(command.playerId) >> player
        and:
            Game.Exception ex = thrown()
            ex.message == 'Game started, can not add new player'
        and:
            repository.repository.size() == 1
        and:
            repository.repository.get(game.id) == game
    }

    def 'should NOT add player to game and throw exception when trying add player which is already in game'() {
        given:
            def game = putGameInRepo()
        and:
            def player = game.players.first()
        and:
            def command = createAddPlayerToGameCommand(new AddPlayerToGameCommandCreator(game, player))
        when:
            service.addPlayer(command)
        then:
            1 * client.getPlayer(command.playerId) >> player
        and:
            Game.Exception ex = thrown()
            ex.message == "Player: [$player] already added to game!"
        and:
            repository.repository.size() == 1
        and:
            repository.repository.get(game.id) == game
    }

    def 'should NOT add player to game and throw exception when trying add player to full game'() {
        given: '5 players'
            def players = (1..5).toList().collect { createPlayerInGame() }
        and: 'game'
            def game = putGameInRepo(new GameCreator(
                players: players
            ))
        and:
            def player = createPlayerInGame()
        and:
            def command = createAddPlayerToGameCommand(new AddPlayerToGameCommandCreator(game, player))
        when:
            service.addPlayer(command)
        then:
            1 * client.getPlayer(command.playerId) >> player
        and:
            Game.Exception ex = thrown()
            ex.message == "Game is full with: [${game.players.size()}] players. Can not add more players!"
        and:
            repository.repository.size() == 1
        and:
            repository.repository.get(game.id) == game
    }

    def '''should start game which have at least one player -
           deal cards - two cards for each player, and set game status to STARTED '''() {
        given:
            def game = putGameInRepo()
        and:
            def command = createStartGameCommand(new StartGameCommandCreator(gameId: game.id))
        when:
            def startedGame = service.start(command)
        then:
            repository.repository.size() == 1
        and:
            repository.repository.get(game.id) == startedGame
        and:
            with(startedGame) {
                status == Game.Status.STARTED
                deck.cards.size() == 52 - (game.players.size() * 2)
                players.each {
                    assert it.hand.size() == 2
                }
            }
    }

    def 'should NOT start game and throw exception when can not find game with gameId'() {
        given:
            def game = putGameInRepo()
        and:
            def command = createStartGameCommand(new StartGameCommandCreator(gameId: 'wrong-game-id'))
        when:
            service.start(command)
        then:
            GameRepository.GameNotFoundException ex = thrown()
            ex.message == 'Game with id: [wrong-game-id] not found.'
        and:
            repository.repository.size() == 1
        and:
            repository.repository.get(game.id) == game
    }

    def 'should NOT start game when no players added to game and throw exception'() {
        given:
            def game = putGameInRepo(new GameCreator(players: [createPlayerInGame()]))
        and:
            def command = createStartGameCommand(new StartGameCommandCreator(gameId: game.id))
        when:
            service.start(command)
        then:
            Game.Exception ex = thrown()
            ex.message == 'Please add at least one player before start.'
        and:
            repository.repository.get(game.id) == game
    }

    def 'should throw exception when trying start game which is already started'() {
        given:
            def game = putGameInRepo(new GameCreator(status: Game.Status.STARTED))
        and:
            def command = createStartGameCommand(new StartGameCommandCreator(gameId: game.id))
        when:
            service.start(command)
        then:
            Game.Exception ex = thrown()
            ex.message == 'Game started, can not start again'
        and:
            repository.repository.get(game.id) == game
    }

    def '''player should make HIT move - draw card and set Game.Move to HIT, if it is current player
        and game should be updated in repository after it'''() {
        given:
            def game = putGameInRepo(new GameCreator(status: Game.Status.STARTED))
        and:
            def player = game.players.find { it != game.croupier }
        and:
            def command = createMakeGameMoveCommand(new MakeGameMoveCommandCreator(game, player, Game.Move.HIT))
        when:
            Game gameAfterMove = service.makeMove(command)
        then:
            repository.repository.size() == 1
        and:
            repository.repository.get(game.id) == gameAfterMove
        and:
            with(gameAfterMove) {
                deck.cards.size() == 51 //because cards wasn't distributed on the game beginning
                with(players.find { it.playerId == player.playerId }) {
                    hand.size() == 1
                    move == command.move
                }
                with(croupier) {
                    hand.isEmpty()
                    !move
                }
            }
    }

    def '''player should make STAND move and when it was last player in game, game should be ended
        and game should be updated in repository after it'''() {
        given:
            def game = putGameInRepo(new GameCreator(
                status: Game.Status.STARTED,
                players: [
                    croupier,
                    createPlayerInGame(new PlayerInGameCreator(move: Game.Move.STAND)),
                    createPlayerInGame()
                ],
                croupierId: croupier.playerId
            ))
        and:
            def player = game.players.find { it != game.croupier && it.move != Game.Move.STAND }
        and:
            def secondPlayer = game.players.find { it != game.croupier && it != player }
        and:
            def command = createMakeGameMoveCommand(new MakeGameMoveCommandCreator(game, player, Game.Move.STAND))
        when:
            Game gameAfterMove = service.makeMove(command)
        then:
            repository.repository.size() == 1
        and:
            repository.repository.get(game.id) == gameAfterMove
        and:
            with(gameAfterMove) {
                with(players.find { it.playerId == player.playerId }) {
                    hand.isEmpty()
                    move == command.move
                }
                with(players.find { it.playerId == secondPlayer.playerId }) {
                    hand.isEmpty()
                    move == Game.Move.STAND
                }
                with(players.find { it.playerId == game.croupier.playerId }) {
                    handValue() >= 17
                    move == Game.Move.STAND
                }
                with(croupier) {
                    handValue() >= 17
                    move == Game.Move.STAND
                }
                status == Game.Status.ENDED
            }
    }

    def '''player should make STAND move, game should not be ENDED when other player can make move
        and game should be updated in repository after it'''() {
        given:
            def game = putGameInRepo(new GameCreator(
                status: Game.Status.STARTED,
                players: [
                    croupier,
                    createPlayerInGame(),
                    createPlayerInGame()
                ],
                croupierId: croupier.playerId
            ))
        and:
            def player = game.players.find { it != game.croupier }
        and:
            def secondPlayer = game.players.find { it != game.croupier && it != player }
        and:
            def command = createMakeGameMoveCommand(new MakeGameMoveCommandCreator(game, player, Game.Move.STAND))
        when:
            Game gameAfterMove = service.makeMove(command)
        then:
            repository.repository.size() == 1
        and:
            repository.repository.get(game.id) == gameAfterMove
        and:
            with(gameAfterMove) {
                with(players.find { it.playerId == player.playerId }) {
                    hand.isEmpty()
                    move == command.move
                }
                with(players.find { it.playerId == secondPlayer.playerId }) {
                    hand.isEmpty()
                    !move
                }
                with(players.find { it.playerId == game.croupier.playerId }) {
                    hand.isEmpty()
                    !move
                }
                with(croupier) {
                    hand.isEmpty()
                    !move
                }
                status == Game.Status.STARTED
            }
    }

    def 'should throw exception when can not find game with gameId in game repository when trying make move'() {
        given:
            def game = putGameInRepo()
        and:
            def command = createMakeGameMoveCommand()
        when:
            service.makeMove(command)
        then:
            GameRepository.GameNotFoundException ex = thrown()
            ex.message == "Game with id: [$command.gameId] not found."
        and:
            repository.repository.get(game.id) == game
    }

    def 'should not be possible to make move when game was not started'() {
        given:
            def game = putGameInRepo()
        and:
            def player = game.players.find { it != game.croupier }
        and:
            def command = createMakeGameMoveCommand(new MakeGameMoveCommandCreator(game, player, Game.Move.STAND))
        when:
            service.makeMove(command)
        then:
            Game.Exception ex = thrown()
            ex.message == 'Game NOT started, please start game before auction'
        and:
            repository.repository.get(game.id) == game
    }

    def 'should not be possible to make move when game was ended'() {
        given:
            def game = putGameInRepo(new GameCreator(status: Game.Status.ENDED))
        and:
            def player = game.players.find { it != game.croupier }
        and:
            def command = createMakeGameMoveCommand(new MakeGameMoveCommandCreator(game, player, Game.Move.STAND))
        when:
            service.makeMove(command)
        then:
            Game.Exception ex = thrown()
            ex.message == 'Game finished. Now you can check results!'
        and:
            repository.repository.get(game.id) == game
    }

    def 'player should not have possibility to make move when is not his turn'() {
        given:
            def game = putGameInRepo(new GameCreator(status: Game.Status.STARTED))
        and:
            def player = game.players.find { it != game.croupier }
        and:
            def command = createMakeGameMoveCommand(new MakeGameMoveCommandCreator(game, game.croupier, Game.Move.HIT))
        when:
            service.makeMove(command)
        then:
            Game.Exception ex = thrown()
            ex.message == "Waiting for move from player: [$player.playerId] instead of: [$game.croupier.playerId]"
        and:
            repository.repository.get(game.id) == game
    }

    def 'should find game in repository and return game results when game is ended'() {
        given:
            def game = putGameInRepo(new GameCreator(status: Game.Status.ENDED))
        and:
            def command = createGetGameResultsCommand(new GetGameResultsCommandCreator(gameId: game.id))
        when:
            List<Result> results = service.getResults(command)
        then:
            results.size() == 2
    }

    def 'should throw exception when can not find game in repository with gameId when getting results'() {
        given:
            def game = putGameInRepo(new GameCreator(status: Game.Status.ENDED))
        and:
            def command = createGetGameResultsCommand(new GetGameResultsCommandCreator(gameId: 'wrong-game-id'))
        when:
            service.getResults(command)
        then:
            GameRepository.GameNotFoundException ex = thrown()
            ex.message == "Game with id: [${command.gameId}] not found."
        and:
            repository.repository.get(game.id) == game
    }

    def 'should not be possible get results from game which is not ended'() {
        given:
            def game = putGameInRepo()
        and:
            def command = createGetGameResultsCommand(new GetGameResultsCommandCreator(gameId: game.id))
        when:
            service.getResults(command)
        then:
            Result.Exception ex = thrown()
            ex.message == 'Results are available only when game finished. Please continue auction.'
        and:
            repository.repository.get(game.id) == game
    }

    def 'should throw null pointer exception when creating game service with null game repo'() {
        when:
            GameServiceFactory.create(null, client)
        then:
            NullPointerException ex = thrown()
            ex.message == 'gameRepo is marked non-null but is null'
    }

    def 'should throw null pointer exception when creating game service with null player service'() {
        when:
            GameServiceFactory.create(repository, null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'playerServiceClient is marked non-null but is null'
    }

    def 'should throw null pointer exception when add player command is null'() {
        when:
            service.addPlayer(null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'addPlayerToGameCommand is marked non-null but is null'
    }

    def 'should throw null pointer exception when create game command is null'() {
        when:
            service.create(null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'createGameCommand is marked non-null but is null'
    }

    def 'should throw null pointer exception when starting game with null command'() {
        when:
            service.start(null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'startGameCommand is marked non-null but is null'
    }

    def 'should throw null pointer exception when move command is null'() {
        when:
            service.makeMove(null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'makeGameMoveCommand is marked non-null but is null'
    }

    def 'should throw null pointer exception when get game results command is null'() {
        when:
            service.getResults(null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'getGameResultsCommand is marked non-null but is null'
    }

    def 'should throw null pointer exception when creating move command with null gameId'() {
        when:
            createMakeGameMoveCommand(new MakeGameMoveCommandCreator(gameId: null))
        then:
            NullPointerException ex = thrown()
            ex.message == 'gameId is marked non-null but is null'
    }

    def 'should throw null pointer exception when creating move command with null playerId'() {
        when:
            createMakeGameMoveCommand(new MakeGameMoveCommandCreator(playerId: null))
        then:
            NullPointerException ex = thrown()
            ex.message == 'playerId is marked non-null but is null'
    }

    def 'should throw null pointer exception when creating move command with null move'() {
        when:
            createMakeGameMoveCommand(new MakeGameMoveCommandCreator(move: null))
        then:
            NullPointerException ex = thrown()
            ex.message == 'move is marked non-null but is null'
    }

    def 'should throw null pointer exception when creating add player command with null gameId'() {
        when:
            createAddPlayerToGameCommand(new AddPlayerToGameCommandCreator(gameId: null))
        then:
            NullPointerException ex = thrown()
            ex.message == 'gameId is marked non-null but is null'
    }

    def 'should throw null pointer exception when creating add player command with null playerId'() {
        when:
            createAddPlayerToGameCommand(new AddPlayerToGameCommandCreator(playerId: null))
        then:
            NullPointerException ex = thrown()
            ex.message == 'playerId is marked non-null but is null'
    }

    def 'should throw null pointer exception when creating start game command with null gameId'() {
        when:
            createStartGameCommand(new StartGameCommandCreator(gameId: null))
        then:
            NullPointerException ex = thrown()
            ex.message == 'gameId is marked non-null but is null'
    }

    def 'should throw null pointer exception when creating get game results command with null gameId'() {
        when:
            createGetGameResultsCommand(new GetGameResultsCommandCreator(gameId: null))
        then:
            NullPointerException ex = thrown()
            ex.message == 'gameId is marked non-null but is null'
    }

    def putGameInRepo(GameCreator creator = new GameCreator()) {
        Game game = createGame(creator)
        repository.repository.put(game.id, game)
        return game
    }
}
