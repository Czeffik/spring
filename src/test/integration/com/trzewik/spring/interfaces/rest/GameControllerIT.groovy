package com.trzewik.spring.interfaces.rest

import com.trzewik.spring.domain.deck.Deck
import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.GameException
import com.trzewik.spring.domain.game.GameRepository
import com.trzewik.spring.domain.game.Result
import com.trzewik.spring.domain.game.ResultCreation
import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.service.GameService
import groovy.json.JsonSlurper
import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles(['test-rest'])
@SpringBootTest(
    classes = [RestConfiguration, TestRestConfig],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class GameControllerIT extends Specification implements GameRequestSender, ResultCreation {
    @Autowired
    GameService service

    JsonSlurper slurper = new JsonSlurper()

    def 'should create game successfully and return game object representation in in response'() {
        given:
        Game game = createStartedGame()

        when:
        Response response = createGameRequest()

        then:
        1 * service.createGame() >> game

        and:
        response.statusCode() == 200

        and:
        with(slurper.parseText(response.body().asString())) {
            id == game.id
            status == game.status.name()
            with(currentPlayer) {
                id == game.currentPlayer.id
                name == game.currentPlayer.name
                move == game.currentPlayer.move.name()
                with(hand) {
                    handValue == game.currentPlayer.handValue()
                    cards.size() == game.currentPlayer.hand.size()
                    validateHand(game.currentPlayer.hand, cards)
                }
            }
            with(croupier) {
                name == game.croupier.name
                with(card) {
                    suit == game.croupier.hand.first().suit.name()
                    rank == game.croupier.hand.first().rank.name()
                }
            }
        }
    }

    def 'should add player to game and return it in response'() {
        given:
        String gameId = 'example-game-id'

        and:
        Player player = createPlayer()

        when:
        Response response = addPlayerRequest(player.name, gameId)

        then:
        1 * service.addPlayer(gameId, player.getName()) >> player

        and:
        response.statusCode() == 200

        and:
        with(slurper.parseText(response.body().asString())) {
            id == player.id
            name == player.name
            move == player.move.name()
            with(hand) {
                handValue == player.handValue()
                cards.size() == player.hand.size()
                validateHand(player.hand, cards)
            }
        }
    }

    def 'should return NOT_FOUND with message when GameNotFoundException is thrown - adding new player to game'() {
        given:
        String gameId = 'example-game-id'

        when:
        Response response = addPlayerRequest('playerName', gameId)

        then:
        1 * service.addPlayer(gameId, 'playerName') >> { throw new GameRepository.GameNotFoundException(gameId) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == "Game with id: [${gameId}] not found."
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - adding new player to game'() {
        given:
        String gameId = 'example-game-id'

        and:
        String exceptionMessage = 'exception message which should be returned by controller'

        when:
        Response response = addPlayerRequest('playerName', gameId)

        then:
        1 * service.addPlayer(gameId, 'playerName') >> { throw new GameException(exceptionMessage) }

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
        1 * service.startGame(game.id) >> game

        and:
        response.statusCode() == 200

        and:
        with(slurper.parseText(response.body().asString())) {
            id == game.id
            status == game.status.name()
            with(currentPlayer) {
                id == game.currentPlayer.id
                name == game.currentPlayer.name
                move == game.currentPlayer.move.name()
                with(hand) {
                    handValue == game.currentPlayer.handValue()
                    cards.size() == game.currentPlayer.hand.size()
                    validateHand(game.currentPlayer.hand, cards)
                }
            }
            with(croupier) {
                name == game.croupier.name
                with(card) {
                    suit == game.croupier.hand.first().suit.name()
                    rank == game.croupier.hand.first().rank.name()
                }
            }
        }
    }

    def 'should return NOT_FOUND with message when GameNotFoundException is thrown - starting game'() {
        given:
        String gameId = 'example-game-id'

        when:
        Response response = startGameRequest(gameId)

        then:
        1 * service.startGame(gameId) >> { throw new GameRepository.GameNotFoundException(gameId) }

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
        1 * service.startGame(gameId) >> { throw new GameException(exceptionMessage) }

        and:
        response.statusCode() == 400

        and:
        response.body().asString() == exceptionMessage
    }

    def 'should make move successfully and return game object representation in response'() {
        given:
        Game game = createStartedGame()

        and:
        Player player = game.currentPlayer

        and:
        Game.Move playerMove = Game.Move.STAND

        when:
        Response response = makeMoveRequest(game.id, player.id, playerMove.name())

        then:
        1 * service.makeMove(game.id, player.id, playerMove) >> game

        and:
        with(slurper.parseText(response.body().asString())) {
            id == game.id
            status == game.status.name()
            with(currentPlayer) {
                id == game.currentPlayer.id
                name == game.currentPlayer.name
                move == game.currentPlayer.move.name()
                with(hand) {
                    handValue == game.currentPlayer.handValue()
                    cards.size() == game.currentPlayer.hand.size()
                    validateHand(game.currentPlayer.hand, cards)
                }
            }
            with(croupier) {
                name == game.croupier.name
                with(card) {
                    suit == game.croupier.hand.first().suit.name()
                    rank == game.croupier.hand.first().rank.name()
                }
            }
        }
    }

    def 'should return NOT_FOUND with message when GameNotFoundException is thrown - player move'() {
        given:
        String gameId = 'example-game-id'

        when:
        Response response = makeMoveRequest(gameId, 'player-id', 'STAND')

        then:
        1 * service.makeMove(gameId, 'player-id', Game.Move.STAND) >> { throw new GameRepository.GameNotFoundException(gameId) }

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
        1 * service.makeMove(gameId, 'player-id', Game.Move.STAND) >> { throw new GameException(exceptionMessage) }

        and:
        response.statusCode() == 400

        and:
        response.body().asString() == exceptionMessage
    }

    def 'should get results successfully and return Result object representation in response'() {
        given:
        String gameId = 'example-game-id'

        and:
        List<Result> expectedResults = createResults(3)

        when:
        Response response = getResultsRequest(gameId)

        then:
        1 * service.getGameResults(gameId) >> expectedResults

        and:
        response.statusCode() == 200

        and:
        with(slurper.parseText(response.body().asString())) {
            results.size() == expectedResults.size()
            validateResults(expectedResults, results)
        }
    }

    def 'should return NOT_FOUND with message when GameNotFoundException is thrown - get results'() {
        given:
        String gameId = 'example-game-id'

        when:
        Response response = getResultsRequest(gameId)

        then:
        1 * service.getGameResults(gameId) >> { throw new GameRepository.GameNotFoundException(gameId) }

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
        1 * service.getGameResults(gameId) >> { throw new GameException(exceptionMessage) }

        and:
        response.statusCode() == 400

        and:
        response.body().asString() == exceptionMessage
    }

    boolean validateHand(Set<Deck.Card> hand, parsedCards) {
        parsedCards.each { parsedCard ->
            assert hand.any { it.equals(createCard(new CardBuilder(parsedCard.suit, parsedCard.rank))) }
        }
        true
    }

    boolean validateResults(List<Result> results, parsedResults) {
        parsedResults.each { parsedResult ->
            assert results.any { result -> result.equals(createResultFrom(parsedResult)) }
        }
        true
    }

    Result createResultFrom(parsedResult) {
        return createResult(new ResultBuilder(
            place: parsedResult.place,
            player: new PlayerBuilder(
                id: parsedResult.player.id,
                name: parsedResult.player.name,
                hand: parsedResult.player.hand.cards.collect { card -> createCard(new CardBuilder(card.suit, card.rank)) } as Set,
                move: Game.Move.valueOf(parsedResult.player.move)
            )
        )
        )
    }

    RequestSpecification request(String basePath) {
        return RestAssured.given()
            .baseUri("http://localhost:${port}")
            .basePath(basePath)
            .log().all()
    }
}
