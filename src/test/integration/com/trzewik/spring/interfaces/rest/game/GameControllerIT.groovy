package com.trzewik.spring.interfaces.rest.game

import com.trzewik.spring.domain.game.Card
import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.GameException
import com.trzewik.spring.domain.game.GameRepository
import com.trzewik.spring.domain.game.GameService
import com.trzewik.spring.domain.game.Move
import com.trzewik.spring.domain.game.ResultCreation
import com.trzewik.spring.domain.player.PlayerCreation
import com.trzewik.spring.domain.player.PlayerRepository
import com.trzewik.spring.domain.player.PlayerService
import com.trzewik.spring.interfaces.rest.RestConfiguration
import com.trzewik.spring.interfaces.rest.TestRestConfig
import groovy.json.JsonSlurper
import io.restassured.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles(['test-rest', 'test'])
@SpringBootTest(
    classes = [RestConfiguration.class, TestRestConfig.class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class GameControllerIT extends Specification implements GameRequestSender, ResultCreation, PlayerCreation {
    @Autowired
    GameService gameService
    @Autowired
    PlayerService playerService

    JsonSlurper slurper = new JsonSlurper()

    def 'should create game successfully and return game object representation in response'() {
        given:
        def game = createStartedGame()
        def croup = createPlayer(new PlayerBuilder(game.croupier))

        when:
        Response response = createGameRequest()

        then:
        1 * playerService.createCroupier() >> croup
        1 * gameService.create(croup) >> game

        and:
        response.statusCode() == 200

        and:
        with(slurper.parseText(response.body().asString())) {
            id == game.id
            status == game.status.name()
            with(currentPlayer) {
                id == game.currentPlayerId
                move == game.currentPlayer.move.name()
                with(hand) {
                    handValue == game.currentPlayer.handValue()
                    cards.size() == game.currentPlayer.hand.size()
                    validateHand(game.currentPlayer.hand, cards)
                }
            }
            with(croupier) {
                id == game.croupierId
                with(card) {
                    suit == game.croupier.hand.first().suit.name()
                    rank == game.croupier.hand.first().rank.name()
                }
            }
        }
    }

    def 'should add player to game if found in player repository and return game representation'() {
        given:
        def game = createStartedGame()
        def currPlayer = createPlayer(new PlayerBuilder(game.currentPlayer))

        when:
        Response response = addPlayerRequest(game.id, game.currentPlayerId)

        then:
        1 * playerService.get(game.currentPlayerId) >> currPlayer

        and:
        1 * gameService.addPlayer(game.id, currPlayer) >> game

        and:
        response.statusCode() == 200

        and:
        with(slurper.parseText(response.body().asString())) {
            id == game.id
            status == game.status.name()
            with(currentPlayer) {
                id == game.currentPlayerId
                move == game.currentPlayer.move.name()
                with(hand) {
                    handValue == game.currentPlayer.handValue()
                    cards.size() == game.currentPlayer.hand.size()
                    validateHand(game.currentPlayer.hand, cards)
                }
            }
            with(croupier) {
                id == game.croupierId
                with(card) {
                    suit == game.croupier.hand.first().suit.name()
                    rank == game.croupier.hand.first().rank.name()
                }
            }
        }
    }

    def '''should return NOT_FOUND with message when PlayerNotFoundException is thrown - player not found in repository
        adding new player to game'''() {
        given:
        def playerId = 'player-id'
        def gameId = 'game-id'

        when:
        Response response = addPlayerRequest(gameId, playerId)

        then:
        1 * playerService.get(playerId) >> { throw new PlayerRepository.PlayerNotFoundException(playerId) }

        and:
        0 * gameService.addPlayer(_)

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == "Can not find player with id: [${playerId}] in repository."
    }

    def '''should return NOT_FOUND with message when GameNotFoundException is thrown - game not found in repository
        adding new player to game'''() {
        given:
        def gameId = 'example-game-id'
        def player = createPlayer(new PlayerBuilder(id: 'player-id'))

        when:
        Response response = addPlayerRequest(gameId, player.id)

        then:
        1 * playerService.get(player.id) >> player
        1 * gameService.addPlayer(gameId, player) >> { throw new GameRepository.GameNotFoundException(gameId) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == "Game with id: [${gameId}] not found."
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - adding new player to game'() {
        given:
        def gameId = 'example-game-id'
        def player = createPlayer(new PlayerBuilder(id: 'player-id'))

        and:
        String exceptionMessage = 'exception message which should be returned by controller'

        when:
        Response response = addPlayerRequest(gameId, player.id)

        then:
        1 * playerService.get(player.id) >> player
        1 * gameService.addPlayer(gameId, player) >> { throw new GameException(exceptionMessage) }

        and:
        response.statusCode() == 400

        and:
        response.body().asString() == exceptionMessage
    }

    def 'should start game successfully and return game object representation in in response'() {
        given:
        Game game = createStartedGame()

        when:
        Response response = startGameRequest(game.id)

        then:
        1 * gameService.start(game.id) >> game

        and:
        response.statusCode() == 200

        and:
        with(slurper.parseText(response.body().asString())) {
            id == game.id
            status == game.status.name()
            with(currentPlayer) {
                id == game.currentPlayerId
                move == game.currentPlayer.move.name()
                with(hand) {
                    handValue == game.currentPlayer.handValue()
                    cards.size() == game.currentPlayer.hand.size()
                    validateHand(game.currentPlayer.hand, cards)
                }
            }
            with(croupier) {
                id == game.croupierId
                with(card) {
                    suit == game.croupier.hand.first().suit.name()
                    rank == game.croupier.hand.first().rank.name()
                }
            }
        }
    }

    def '''should return NOT_FOUND with message when GameNotFoundException is thrown - game not found in repository
        starting game'''() {
        given:
        String gameId = 'example-game-id'

        when:
        Response response = startGameRequest(gameId)

        then:
        1 * gameService.start(gameId) >> { throw new GameRepository.GameNotFoundException(gameId) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == "Game with id: [${gameId}] not found."
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - starting game'() {
        given:
        String gameId = 'example-game-id'

        and:
        String exceptionMessage = 'exception message which should be returned by controller'

        when:
        Response response = startGameRequest(gameId)

        then:
        1 * gameService.start(gameId) >> { throw new GameException(exceptionMessage) }

        and:
        response.statusCode() == 400

        and:
        response.body().asString() == exceptionMessage
    }

    def 'should make move successfully and return game object representation in response'() {
        given:
        def game = createStartedGame()

        and:
        def playerId = game.currentPlayerId

        and:
        Move playerMove = Move.STAND

        when:
        Response response = makeMoveRequest(game.id, playerId, playerMove.name())

        then:
        1 * gameService.makeMove(game.id, playerId, playerMove) >> game

        and:
        with(slurper.parseText(response.body().asString())) {
            id == game.id
            status == game.status.name()
            with(currentPlayer) {
                id == game.currentPlayerId
                move == game.currentPlayer.move.name()
                with(hand) {
                    handValue == game.currentPlayer.handValue()
                    cards.size() == game.currentPlayer.hand.size()
                    validateHand(game.currentPlayer.hand, cards)
                }
            }
            with(croupier) {
                id == game.croupierId
                with(card) {
                    suit == game.croupier.hand.first().suit.name()
                    rank == game.croupier.hand.first().rank.name()
                }
            }
        }
    }

    def '''should return NOT_FOUND with message when GameNotFoundException is thrown - game not found in repository
        player move'''() {
        given:
        String gameId = 'example-game-id'

        when:
        Response response = makeMoveRequest(gameId, 'player-id', 'STAND')

        then:
        1 * gameService.makeMove(gameId, 'player-id', Move.STAND) >> { throw new GameRepository.GameNotFoundException(gameId) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == "Game with id: [${gameId}] not found."
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - player move'() {
        given:
        String gameId = 'example-game-id'

        and:
        String exceptionMessage = 'exception message which should be returned by controller'

        when:
        Response response = makeMoveRequest(gameId, 'player-id', 'STAND')

        then:
        1 * gameService.makeMove(gameId, 'player-id', Move.STAND) >> { throw new GameException(exceptionMessage) }

        and:
        response.statusCode() == 400

        and:
        response.body().asString() == exceptionMessage
    }

    def 'should get results successfully and return Result object representation in response'() {
        given:
        def gameId = 'example-game-id'

        and:
        def expectedResults = createResults(3)

        when:
        Response response = getResultsRequest(gameId)

        then:
        1 * gameService.getResults(gameId) >> expectedResults

        and:
        response.statusCode() == 200

        and:
        with(slurper.parseText(response.body().asString())) {
            results.size() == expectedResults.size()
        }
        //TODO add clever validation
    }

    def 'should return NOT_FOUND with message when GameNotFoundException is thrown - get results'() {
        given:
        String gameId = 'example-game-id'

        when:
        Response response = getResultsRequest(gameId)

        then:
        1 * gameService.getResults(gameId) >> { throw new GameRepository.GameNotFoundException(gameId) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == "Game with id: [${gameId}] not found."
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - get results'() {
        given:
        String gameId = 'example-game-id'

        and:
        String exceptionMessage = 'exception message which should be returned by controller'

        when:
        Response response = getResultsRequest(gameId)

        then:
        1 * gameService.getResults(gameId) >> { throw new GameException(exceptionMessage) }

        and:
        response.statusCode() == 400

        and:
        response.body().asString() == exceptionMessage
    }

    boolean validateHand(Set<Card> hand, parsedCards) {
        parsedCards.each { parsedCard ->
            assert hand.any { it.equals(createCard(new CardBuilder(parsedCard.suit, parsedCard.rank))) }
        }
        true
    }
}
