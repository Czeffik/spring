package com.trzewik.spring.interfaces.rest.game

import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.GameCreation
import com.trzewik.spring.domain.game.GameRepository
import com.trzewik.spring.domain.game.GameService
import com.trzewik.spring.domain.game.Result
import com.trzewik.spring.domain.game.ResultCreation
import com.trzewik.spring.domain.player.PlayerCreation
import com.trzewik.spring.domain.player.PlayerRepository
import com.trzewik.spring.domain.player.PlayerService
import com.trzewik.spring.interfaces.rest.ErrorResponseValidator
import com.trzewik.spring.interfaces.rest.RestConfiguration
import com.trzewik.spring.interfaces.rest.TestRestConfig
import groovy.json.JsonSlurper
import io.restassured.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@ActiveProfiles(['test-rest', 'test'])
@SpringBootTest(
    classes = [RestConfiguration.class, TestRestConfig.class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class GameControllerIT extends Specification implements GameRequestSender, ResultCreation, PlayerCreation,
    GameCreation, ErrorResponseValidator, GameResponseValidator {
    @Shared
    JsonSlurper jsonSlurper = new JsonSlurper()

    @Autowired
    GameService gameService
    @Autowired
    PlayerService playerService
    @LocalServerPort
    int port

    def 'should create game successfully and return game object representation in response'() {
        given:
            def game = createGame(new GameCreator().startedGame())
        when:
            def response = createGameRequest()
        then:
            1 * gameService.create(_ as GameService.CreateGameCommand) >> game
        and:
            response.statusCode() == 200
        and:
            validateGameResponse(response, game)
    }

    def 'should add player to game if found in player repository and return game representation'() {
        given:
            def game = createGame(new GameCreator().startedGame())
        and:
            def currentPlayer = game.currentPlayer
        when:
            def response = addPlayerRequest(game.id, currentPlayer.id)
        then:
            1 * gameService.addPlayer({ GameService.AddPlayerToGameCommand command ->
                assert command.gameId == game.id
                assert command.playerId == currentPlayer.id
            }) >> game
        and:
            response.statusCode() == 200
        and:
            validateGameResponse(response, game)
    }

    def '''should return NOT_FOUND with message when PlayerNotFoundException is thrown - player not found in repository
        adding new player to game'''() {
        given:
            def playerId = 'player-id'
            def gameId = 'game-id'
        when:
            def response = addPlayerRequest(gameId, playerId)
        then:
            1 * gameService.addPlayer({ GameService.AddPlayerToGameCommand command ->
                assert command.gameId == gameId
                assert command.playerId == playerId
            }) >> { throw new PlayerRepository.PlayerNotFoundException(playerId) }
        and:
            response.statusCode() == 404
        and:
            validateErrorResponse(response, "Can not find player with id: [${playerId}] in repository.", HttpStatus.NOT_FOUND)
    }

    def '''should return NOT_FOUND with message when GameNotFoundException is thrown - game not found in repository
        adding new player to game'''() {
        given:
            def gameId = 'example-game-id'
            def player = createPlayer()
        when:
            def response = addPlayerRequest(gameId, player.id)
        then:
            1 * gameService.addPlayer({ GameService.AddPlayerToGameCommand command ->
                assert command.gameId == gameId
                assert command.playerId == player.id
            }) >> { throw new GameRepository.GameNotFoundException(gameId) }
        and:
            response.statusCode() == 404
        and:
            validateErrorResponse(response, "Game with id: [${gameId}] not found.", HttpStatus.NOT_FOUND)
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - adding new player to game'() {
        given:
            def gameId = 'example-game-id'
            def player = createPlayer()
        and:
            def exceptionMessage = 'exception message which should be returned by controller'
        when:
            def response = addPlayerRequest(gameId, player.id)
        then:
            1 * gameService.addPlayer({ GameService.AddPlayerToGameCommand command ->
                assert command.gameId == gameId
                assert command.playerId == player.id
            }) >> { throw new Game.Exception(exceptionMessage) }
        and:
            response.statusCode() == 400
        and:
            validateErrorResponse(response, exceptionMessage, HttpStatus.BAD_REQUEST)
    }

    def 'should start game successfully and return game object representation in in response'() {
        given:
            def game = createGame(new GameCreator().startedGame())
        when:
            def response = startGameRequest(game.id)
        then:
            1 * gameService.start({ GameService.StartGameCommand command ->
                assert command.gameId == game.id
            }) >> game
        and:
            response.statusCode() == 200
        and:
            validateGameResponse(response, game)
    }

    def '''should return NOT_FOUND with message when GameNotFoundException is thrown - game not found in repository
        starting game'''() {
        given:
            def gameId = 'example-game-id'
        when:
            def response = startGameRequest(gameId)
        then:
            1 * gameService.start({ GameService.StartGameCommand command ->
                assert command.gameId == gameId
            }) >> { throw new GameRepository.GameNotFoundException(gameId) }
        and:
            response.statusCode() == 404
        and:
            validateErrorResponse(response, "Game with id: [${gameId}] not found.", HttpStatus.NOT_FOUND)
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - starting game'() {
        given:
            def gameId = 'example-game-id'
        and:
            def exceptionMessage = 'exception message which should be returned by controller'
        when:
            def response = startGameRequest(gameId)
        then:
            1 * gameService.start({ GameService.StartGameCommand command ->
                assert command.gameId == gameId
            }) >> { throw new Game.Exception(exceptionMessage) }
        and:
            response.statusCode() == 400
        and:
            validateErrorResponse(response, exceptionMessage, HttpStatus.BAD_REQUEST)
    }

    @Unroll
    def 'should make move: #MOVE successfully and return game object representation in response'() {
        given:
            def game = createGame(new GameCreator().startedGame())
        and:
            def player = game.currentPlayer
        when:
            def response = makeMoveRequest(game.id, player.id, MOVE)
        then:
            1 * gameService.makeMove({ GameService.MakeGameMoveCommand command ->
                assert command.playerId == player.id
                assert command.gameId == game.id
                assert command.move == MOVE_ENUM
            }) >> game
        and:
            response.statusCode() == 200
        and:
            validateGameResponse(response, game)
        where:
            MOVE    || MOVE_ENUM
            'HIT'   || Game.Move.HIT
            'STAND' || Game.Move.STAND
    }

    @Unroll
    def 'should return BAD_REQUEST when send request with move which can not be parsed as Game.Move enum: #MOVE'() {
        given:
            def game = createGame(new GameCreator().startedGame())
        and:
            def player = game.currentPlayer
        when:
            def response = makeMoveRequest(game.id, player.id, MOVE)
        then:
            0 * gameService.makeMove(_)
        and:
            response.statusCode() == 400
        and:
            validateErrorResponseMatch(
                response,
                'Failed to convert value of type.* to required type.*com\\.trzewik\\.spring\\.domain\\.game\\.Game\\$Move.*',
                HttpStatus.BAD_REQUEST
            )
        where:
            MOVE    | _
            'hit'   | _
            'stand' | _
            '1'     | _
    }

    def '''should return NOT_FOUND with message when GameNotFoundException is thrown - game not found in repository
        player move'''() {
        given:
            def gameId = 'example-game-id'
            def playerId = 'example-id'
        when:
            def response = makeMoveRequest(gameId, playerId, 'HIT')
        then:
            1 * gameService.makeMove({ GameService.MakeGameMoveCommand command ->
                assert command.playerId == playerId
                assert command.gameId == gameId
                assert command.move == Game.Move.HIT
            }) >> { throw new GameRepository.GameNotFoundException(gameId) }
        and:
            response.statusCode() == 404
        and:
            validateErrorResponse(response, "Game with id: [${gameId}] not found.", HttpStatus.NOT_FOUND)
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - player move'() {
        given:
            def gameId = 'example-game-id'
            def playerId = 'example-id'
        and:
            String exceptionMessage = 'exception message which should be returned by controller'
        when:
            def response = makeMoveRequest(gameId, playerId, 'HIT')
        then:
            1 * gameService.makeMove({ GameService.MakeGameMoveCommand command ->
                assert command.playerId == playerId
                assert command.gameId == gameId
                assert command.move == Game.Move.HIT
            }) >> { throw new Game.Exception(exceptionMessage) }
        and:
            response.statusCode() == 400
        and:
            validateErrorResponse(response, exceptionMessage, HttpStatus.BAD_REQUEST)
    }

    def 'should get results successfully and return Result object representation in response'() {
        given:
            def gameId = 'example-game-id'
        and:
            def expectedResults = [createResult()]
        when:
            Response response = getResultsRequest(gameId)
        then:
            1 * gameService.getResults({ GameService.GetGameResultsCommand command ->
                assert command.gameId == gameId
            }) >> expectedResults
        and:
            response.statusCode() == 200
        and:
            validateResults(response, expectedResults)
    }

    def 'should return NOT_FOUND with message when GameNotFoundException is thrown - get results'() {
        given:
            String gameId = 'example-game-id'
        when:
            Response response = getResultsRequest(gameId)
        then:
            1 * gameService.getResults({ GameService.GetGameResultsCommand command ->
                assert command.gameId == gameId
            }) >> { throw new GameRepository.GameNotFoundException(gameId) }
        and:
            response.statusCode() == 404
        and:
            validateErrorResponse(response, "Game with id: [${gameId}] not found.", HttpStatus.NOT_FOUND)
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - get results'() {
        given:
            String gameId = 'example-game-id'
        and:
            String exceptionMessage = 'exception message which should be returned by controller'
        when:
            Response response = getResultsRequest(gameId)
        then:
            1 * gameService.getResults({ GameService.GetGameResultsCommand command ->
                assert command.gameId == gameId
            }) >> { throw new Result.Exception(exceptionMessage) }
        and:
            response.statusCode() == 400
        and:
            validateErrorResponse(response, exceptionMessage, HttpStatus.BAD_REQUEST)
    }

    @Override
    JsonSlurper getSlurper() {
        return jsonSlurper
    }
}
