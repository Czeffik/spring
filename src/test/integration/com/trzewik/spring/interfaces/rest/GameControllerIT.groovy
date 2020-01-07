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
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Ignore
import spock.lang.Specification

@Ignore('until rest configuration not working correctly these tests must be ignored :C')
@ActiveProfiles(['test-rest'])
@ContextConfiguration(
    classes = [TestRestConfig]
)
class GameControllerIT extends Specification implements ResultCreation {
    @Autowired
    GameService service

    JsonSlurper slurper = new JsonSlurper()

    def 'should create game successfully and return game object representation in in response'() {
        given:
        Game game = createGame(new GameBuilder([
            new PlayerBuilder(hand: [createCard(Deck.Card.Rank.FOUR), createCard(Deck.Card.Rank.SEVEN)]),
            new PlayerBuilder(hand: [createCard(Deck.Card.Rank.EIGHT), createCard(Deck.Card.Rank.QUEEN)])
        ]))

        when:
        Response response = request('/games')
            .post()
            .thenReturn()

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
                move == game.currentPlayer.move
                with(hand) {
                    handValue == game.currentPlayer.handValue()
                    cards.size() == game.currentPlayer.hand.size()
                    validateHand(game.currentPlayer.hand, cards)
                }
            }
            with(croupier) {
                name == game.croupier.name
                with(card) {
                    suit == game.croupier.hand.first().suit
                    rank == game.croupier.hand.first().rank
                }
            }
        }
    }

    def 'should add player to game and return it in response'() {
        given:
        String gameId = 'example-game-id'

        and:
        Player player = createPlayer()

        and:
        String addPlayerForm = """{"name": "${player.name}"}"""

        when:
        Response response = request("/games/${gameId}/players")
            .body(addPlayerForm)
            .post()
            .thenReturn()

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

        and:
        String addPlayerForm = """{"name": "playerName"}"""

        when:
        Response response = request("/games/${gameId}/players")
            .body(addPlayerForm)
            .post()
            .thenReturn()

        then:
        1 * service.addPlayer(gameId, 'playerName') >> { throw new GameRepository.GameNotFoundException(gameId) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == "Game with id: ${gameId} not found."
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - adding new player to game'() {
        given:
        String gameId = 'example-game-id'

        and:
        String exceptionMessage = 'exception message which should be returned by controller'

        and:
        String addPlayerForm = """{"name": "playerName"}"""

        when:
        Response response = request("/games/${gameId}/players")
            .body(addPlayerForm)
            .post()
            .thenReturn()

        then:
        1 * service.addPlayer(gameId, 'playerName') >> { throw new GameException(exceptionMessage) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == exceptionMessage
    }

    def 'should start game successfully and return game object representation in in response'() {
        given:
        Game game = createGame(new GameBuilder([
            new PlayerBuilder(hand: [createCard(Deck.Card.Rank.FOUR), createCard(Deck.Card.Rank.SEVEN)]),
            new PlayerBuilder(hand: [createCard(Deck.Card.Rank.EIGHT), createCard(Deck.Card.Rank.QUEEN)])
        ]))

        when:
        Response response = request("/games/${game.id}/startGame")
            .post()
            .thenReturn()

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
                move == game.currentPlayer.move
                with(hand) {
                    handValue == game.currentPlayer.handValue()
                    cards.size() == game.currentPlayer.hand.size()
                    validateHand(game.currentPlayer.hand, cards)
                }
            }
            with(croupier) {
                name == game.croupier.name
                with(card) {
                    suit == game.croupier.hand.first().suit
                    rank == game.croupier.hand.first().rank
                }
            }
        }
    }

    def 'should return NOT_FOUND with message when GameNotFoundException is thrown - starting game'() {
        given:
        String gameId = 'example-game-id'

        when:
        Response response = request("/games/${gameId}/startGame")
            .post()
            .thenReturn()

        then:
        1 * service.startGame(gameId) >> { throw new GameRepository.GameNotFoundException(gameId) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == "Game with id: ${gameId} not found."
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - starting game'() {
        given:
        String gameId = 'example-game-id'

        and:
        String exceptionMessage = 'exception message which should be returned by controller'

        when:
        Response response = request("/games/${gameId}/startGame")
            .post()
            .thenReturn()

        then:
        1 * service.startGame(gameId) >> { throw new GameException(exceptionMessage) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == exceptionMessage
    }

    def 'should make move successfully and return game object representation in response'() {
        given:
        Game game = createGame(new GameBuilder([
            new PlayerBuilder(hand: [createCard(Deck.Card.Rank.FOUR), createCard(Deck.Card.Rank.SEVEN)]),
            new PlayerBuilder(hand: [createCard(Deck.Card.Rank.EIGHT), createCard(Deck.Card.Rank.QUEEN)])
        ]))

        and:
        Player player = game.currentPlayer

        and:
        Game.Move move = Game.Move.STAND

        and:
        String moveForm = """{"playerId": "${player.id}", "move": "${move.name()}"}"""

        when:
        Response response = request("/games/${game.id}/move")
            .body(moveForm)
            .post()
            .thenReturn()

        then:
        1 * service.makeMove(game.id, player.id, move) >> game

        and:
        with(slurper.parseText(response.body().asString())) {
            id == game.id
            status == game.status.name()
            with(currentPlayer) {
                id == game.currentPlayer.id
                name == game.currentPlayer.name
                move == game.currentPlayer.move
                with(hand) {
                    handValue == game.currentPlayer.handValue()
                    cards.size() == game.currentPlayer.hand.size()
                    validateHand(game.currentPlayer.hand, cards)
                }
            }
            with(croupier) {
                name == game.croupier.name
                with(card) {
                    suit == game.croupier.hand.first().suit
                    rank == game.croupier.hand.first().rank
                }
            }
        }
    }

    def 'should return NOT_FOUND with message when GameNotFoundException is thrown - player move'() {
        given:
        String gameId = 'example-game-id'

        and:
        String moveForm = """{"playerId": "player-id", "move": "STAND"}"""

        when:
        Response response = request("/games/${gameId}/move")
            .body(moveForm)
            .post()
            .thenReturn()

        then:
        1 * service.makeMove(gameId, 'player-id', 'STAND') >> { throw new GameRepository.GameNotFoundException(gameId) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == "Game with id: ${gameId} not found."
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - player move'() {
        given:
        String gameId = 'example-game-id'

        and:
        String exceptionMessage = 'exception message which should be returned by controller'

        and:
        String moveForm = """{"playerId": "player-id", "move": "STAND"}"""

        when:
        Response response = request("/games/${gameId}/move")
            .body(moveForm)
            .post()
            .thenReturn()

        then:
        1 * service.makeMove(gameId, 'player-id', 'STAND') >> { throw new GameException(exceptionMessage) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == exceptionMessage
    }

    def 'should get results successfully and return Result object representation in response'() {
        given:
        String gameId = 'example-game-id'

        and:
        List<Result> expectedResults = createResults(3)

        when:
        Response response = request("/games/${gameId}/results")
            .get()
            .thenReturn()

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
        Response response = request("/games/${gameId}/results")
            .get()
            .thenReturn()

        then:
        1 * service.getGameResults(gameId) >> { throw new GameRepository.GameNotFoundException(gameId) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == "Game with id: ${gameId} not found."
    }

    def 'should return BAD_REQUEST with message when GameException is thrown - get results'() {
        given:
        String gameId = 'example-game-id'

        and:
        String exceptionMessage = 'exception message which should be returned by controller'

        when:
        Response response = request("/games/${gameId}/results")
            .get()
            .thenReturn()

        then:
        1 * service.getGameResults(gameId) >> { throw new GameException(exceptionMessage) }

        and:
        response.statusCode() == 404

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
            assert results.any {
                it.equals(createResult(
                    new ResultBuilder(
                        place: parsedResult.place,
                        player: new PlayerBuilder(
                            id: player.id,
                            name: player.name,
                            hand: player.hand.cards.collect { createCard(new CardBuilder(it.suit, it.rank)) } as Set,
                            move: Game.Move.valueOf(player.move)
                        )
                    )
                ))
            }
        }
        true
    }

    RequestSpecification request(String basePath) {
        return RestAssured.given()
            .baseUri(getUrl())
            .basePath(basePath)
            .log().all()
    }

    String getUrl() {
        //todo when rest configuration will be fixed, update url (port)
        return "http://localhost:9090"
    }

}
