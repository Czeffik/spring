package com.trzewik.spring.interfaces.rest.player

import com.trzewik.spring.domain.player.PlayerCreation
import com.trzewik.spring.domain.player.PlayerRepository
import com.trzewik.spring.domain.player.PlayerService
import com.trzewik.spring.interfaces.rest.ErrorResponseValidator
import com.trzewik.spring.interfaces.rest.RestConfiguration
import com.trzewik.spring.interfaces.rest.TestRestConfig
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Specification

@ActiveProfiles(['test-rest', 'test'])
@SpringBootTest(
    classes = [RestConfiguration.class, TestRestConfig.class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class PlayerControllerIT extends Specification implements PlayerRequestSender, PlayerCreation, PlayerControllerFormCreation,
    PlayerResponseValidator, ErrorResponseValidator {
    @Shared
    JsonSlurper jsonSlurper = new JsonSlurper()
    @Autowired
    PlayerService service
    @LocalServerPort
    int port

    def 'should create player successfully and return player object representation in response'() {
        given:
            def player = createPlayer()
        when:
            def response = createPlayerRequest(player.name)
        then:
            1 * service.create({ PlayerService.CreatePlayerCommand command ->
                assert command.name == player.name
            }) >> player
        and:
            response.statusCode() == 200
        and:
            validatePlayerResponse(response, player)
    }

    def 'should get player by id successfully and return player representation in response'() {
        given:
            def player = createPlayer()
        when:
            def response = getPlayerRequest(player.id)
        then:
            1 * service.get({ PlayerService.GetPlayerCommand command ->
                assert command.playerId == player.id
            }) >> player
        and:
            response.statusCode() == 200
        and:
            validatePlayerResponse(response, player)
    }

    def 'should return NOT_FOUND with message when PlayerNotFoundException is thrown - get player'() {
        given:
            def playerId = 'example-player-id'
        when:
            def response = getPlayerRequest(playerId)
        then:
            1 * service.get({ PlayerService.GetPlayerCommand command ->
                assert command.playerId == playerId
            }) >> { throw new PlayerRepository.PlayerNotFoundException(playerId) }
        and:
            response.statusCode() == 404
        and:
            validateErrorResponse(response, "Can not find player with id: [${playerId}] in repository.", HttpStatus.NOT_FOUND)
    }

    def 'should return BAD_REQUEST with message when NullPointerException is thrown - get player'() {
        given:
            def playerId = 'example-player-id'
        when:
            def response = getPlayerRequest(playerId)
        then:
            1 * service.get({ PlayerService.GetPlayerCommand command ->
                assert command.playerId == playerId
            }) >> { throw new NullPointerException(playerId) }
        and:
            response.statusCode() == 400
        and:
            validateErrorResponse(response, playerId, HttpStatus.BAD_REQUEST)

    }

    def 'should return INTERNAL_SERVER_ERROR with message when Exception is thrown - get player'() {
        given:
            def playerId = 'example-player-id'
        when:
            def response = getPlayerRequest(playerId)
        then:
            1 * service.get({ PlayerService.GetPlayerCommand command ->
                assert command.playerId == playerId
            }) >> { throw new Exception() }
        and:
            response.statusCode() == 500
        and:
            validateErrorResponse(response, null, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Override
    JsonSlurper getSlurper() {
        return jsonSlurper
    }
}
