package com.trzewik.spring

import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.GameService
import com.trzewik.spring.domain.player.PlayerService
import com.trzewik.spring.infrastructure.db.DbSpec
import com.trzewik.spring.infrastructure.db.game.GameTableInteraction
import com.trzewik.spring.infrastructure.db.game.GameTableVerification
import com.trzewik.spring.infrastructure.db.game.PlayerInGameTableInteraction
import com.trzewik.spring.infrastructure.db.game.PlayerInGameTableVerification
import com.trzewik.spring.infrastructure.db.player.PlayerTableInteraction
import com.trzewik.spring.infrastructure.db.player.PlayerTableVerification
import com.trzewik.spring.interfaces.rest.game.GameRequestSender
import com.trzewik.spring.interfaces.rest.player.PlayerRequestSender
import groovy.json.JsonSlurper
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared

@Slf4j
@ActiveProfiles(['test'])
@SpringBootTest(
    classes = [BlackJackApp],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration(initializers = DbInitializer)
class BlackJackFT extends DbSpec implements GameRequestSender, PlayerRequestSender,
    GameTableInteraction, PlayerInGameTableInteraction, PlayerTableInteraction,
    GameTableVerification, PlayerInGameTableVerification, PlayerTableVerification {
    @LocalServerPort
    int port

    @Shared
    Sql sql
    @Shared
    JsonSlurper jsonSlurper = new JsonSlurper()

    def setupSpec() {
        sql = Sql.newInstance(
            container.jdbcUrl,
            container.username,
            container.password
        )
    }

    def setup() {
        deleteAllPlayersInGame()
        deleteAllPlayers()
        deleteAllGames()
    }

    def cleanup() {
        deleteAllPlayersInGame()
        deleteAllPlayers()
        deleteAllGames()
    }

    def cleanupSpec() {
        sql.close()
    }

    def '''should create new game with croupier and deck
        and add new player to this game
        and start game - shuffle deck, deal cards and select current player,
        and player should make move (STAND)
        and game should end
        and should be possible get game results'''() {
        when:
            def createGameResponse = createGameRequest()
        then:
            createGameResponse.statusCode() == 200
        and:
            getAllGames().size() == 1
            getAllPlayers().size() == 1
            getAllPlayersInGame().size() == 1
        and:
            def gameId = slurper.parseText(createGameResponse.body().asString()).id
        when:
            def createPlayerResponse = createPlayerRequest(new PlayerService.CreateForm(name: 'Adam'))
        then:
            createPlayerResponse.statusCode() == 200
        and:
            getAllGames().size() == 1
            getAllPlayers().size() == 2
            getAllPlayersInGame().size() == 1
        and:
            def playerId = slurper.parseText(createPlayerResponse.body().asString()).id
        when:
            def addPlayerResponse = addPlayerRequest(gameId, playerId)
        then:
            addPlayerResponse.statusCode() == 200
        and:
            getAllPlayers().size() == 2
            getAllPlayersInGame().size() == 2
            getAllGames().size() == 1
        when:
            def startGameResponse = startGameRequest(gameId)
        then:
            startGameResponse.statusCode() == 200
        and:
            getAllPlayersInGame().each { assert slurper.parseText(it.hand.value).size() == 2 }
            getAllGames().first().status == 'STARTED'
        when:
            def playerMoveResponse = makeMoveRequest(gameId, new GameService.MoveForm(playerId: playerId, move: Game.Move.STAND))
        then:
            playerMoveResponse.statusCode() == 200
        and:
            getAllGames().first().status == 'ENDED'
        when:
            def resultsResponse = getResultsRequest(gameId)
        then:
            resultsResponse.statusCode() == 200
        and:
            slurper.parseText(resultsResponse.body().asString()).results.size() == 2
    }

    @Override
    String getDefaultSchema() {
        return DEFAULT_SCHEMA
    }

    @Override
    JsonSlurper getSlurper() {
        return jsonSlurper
    }
}
