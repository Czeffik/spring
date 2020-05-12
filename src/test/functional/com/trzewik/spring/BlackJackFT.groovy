package com.trzewik.spring

import com.trzewik.spring.infrastructure.db.DbSpec
import com.trzewik.spring.interfaces.rest.game.GameRequestSender
import com.trzewik.spring.interfaces.rest.player.PlayerRequestSender
import io.restassured.response.Response
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles(['test'])
@SpringBootTest(
    classes = [BlackJackApp],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration(initializers = DbInitializer)
class BlackJackFT extends DbSpec implements GameRequestSender, PlayerRequestSender {

    def '''should create new game with croupier and deck
        and add new player to this game
        and start game - shuffle deck, deal cards and select current player,
        and player should make move (STAND)
        and game should end
        and should be possible get game results'''() {

        when:
            Response createGameResponse = createGameRequest()

        then:
            createGameResponse.statusCode() == 200

        and:
            helper.getAllGames().size() == 1
            helper.getAllPlayers().size() == 1
            helper.getAllGamesPlayers().size() == 1

        and:
            String gameId = slurper.parseText(createGameResponse.body().asString()).id

        when:
            Response createPlayerResponse = createPlayerRequest('Adam')

        then:
            createPlayerResponse.statusCode() == 200

        and:
            helper.getAllGames().size() == 1
            helper.getAllPlayers().size() == 2
            helper.getAllGamesPlayers().size() == 1

        and:
            String playerId = slurper.parseText(createPlayerResponse.body().asString()).id

        when:
            Response addPlayerResponse = addPlayerRequest(gameId, playerId)

        then:
            addPlayerResponse.statusCode() == 200

        and:
            helper.getAllPlayers().size() == 2
            helper.getAllGamesPlayers().size() == 2
            helper.getAllGames().size() == 1

        when:
            Response startGameResponse = startGameRequest(gameId)

        then:
            startGameResponse.statusCode() == 200

        and:
            helper.getAllGamesPlayers().each { assert slurper.parseText(it.hand.value).size() == 2 }
            helper.getAllGames().first().status == 'STARTED'

        when:
            Response playerMoveResponse = makeMoveRequest(gameId, playerId, 'STAND')

        then:
            playerMoveResponse.statusCode() == 200

        and:
            helper.getAllGames().first().status == 'ENDED'

        when:
            Response resultsResponse = getResultsRequest(gameId)

        then:
            resultsResponse.statusCode() == 200

        and:
            slurper.parseText(resultsResponse.body().asString()).results.size() == 2
    }
}
