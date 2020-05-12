package com.trzewik.spring.interfaces.rest.player

import com.trzewik.spring.domain.player.PlayerCreation
import com.trzewik.spring.domain.player.PlayerRepository
import com.trzewik.spring.domain.player.PlayerService
import com.trzewik.spring.interfaces.rest.RestConfiguration
import com.trzewik.spring.interfaces.rest.TestRestConfig
import groovy.json.JsonSlurper
import io.restassured.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles(['test-rest', 'test'])
@SpringBootTest(
    classes = [RestConfiguration.class, TestRestConfig.class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class PlayerControllerIT extends Specification implements PlayerRequestSender, PlayerCreation {
    @Autowired
    PlayerService service

    @LocalServerPort
    int port

    JsonSlurper slurper = new JsonSlurper()

    def 'should create player successfully and return player object representation in response'() {
        given:
            def player = createPlayer()
        and:
            def form = new PlayerService.CreateForm(name: player.name)
        when:
            def response = createPlayerRequest(form)
        then:
            1 * service.create(form) >> player
        and:
            response.statusCode() == 200
        and:
            with(slurper.parseText(response.body().asString())) {
                id == player.id
                name == player.name
            }
    }

    def 'should get player by id successfully and return player representation in response'() {
        given:
            def player = createPlayer()
        when:
            def response = getPlayerRequest(player.id)
        then:
            1 * service.get(player.id) >> player
        and:
            response.statusCode() == 200
        and:
            with(slurper.parseText(response.body().asString())) {
                id == player.id
                name == player.name
            }
    }

    def 'should return NOT_FOUND with message when PlayerNotFoundException is thrown - get player'() {
        given:
            String playerId = 'example-player-id'
        when:
            Response response = getPlayerRequest(playerId)
        then:
            1 * service.get(playerId) >> { throw new PlayerRepository.PlayerNotFoundException(playerId) }
        and:
            response.statusCode() == 404
        and:
            with(slurper.parseText(response.body().asString())) {
                message == "Can not find player with id: [${playerId}] in repository."
                code == 404
                reason == 'Not Found'
            }
    }

    def 'should return BAD_REQUEST with message when NullPointerException is thrown - get player'() {
        given:
            String playerId = 'example-player-id'
        when:
            Response response = getPlayerRequest(playerId)
        then:
            1 * service.get(playerId) >> { throw new NullPointerException(playerId) }
        and:
            response.statusCode() == 400
        and:
            with(slurper.parseText(response.body().asString())) {
                message == playerId
                code == 400
                reason == 'Bad Request'
            }
    }

    def 'should return INTERNAL_SERVER_ERROR with message when Exception is thrown - get player'() {
        given:
            String playerId = 'example-player-id'
        when:
            Response response = getPlayerRequest(playerId)
        then:
            1 * service.get(playerId) >> { throw new Exception() }
        and:
            response.statusCode() == 500
        and:
            with(slurper.parseText(response.body().asString())) {
                code == 500
                reason == 'Internal Server Error'
            }
    }
}
