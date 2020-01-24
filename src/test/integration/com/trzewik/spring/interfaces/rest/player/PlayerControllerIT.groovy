package com.trzewik.spring.interfaces.rest.player


import com.trzewik.spring.domain.player.Player
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
class PlayerControllerIT extends Specification implements PlayerRequestSender, PlayerCreation {
    @Autowired
    PlayerService service

    JsonSlurper slurper = new JsonSlurper()


    def 'should create player successfully and return player object representation in response'() {
        given:
        Player player = createPlayer()

        when:
        Response response = createPlayerRequest(player.name)

        then:
        1 * service.createPlayer(player.name) >> player

        and:
        response.statusCode() == 200

        and:
        with(slurper.parseText(response.body().asString())) {
            id == player.id
            name == player.name
        }
    }

    def 'should get player by id successfully and return prayer represntation in response'() {
        given:
        Player player = createPlayer()

        when:
        Response response = getPlayerRequest(player.id)

        then:
        1 * service.getPlayer(player.id) >> player

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
        1 * service.getPlayer(playerId) >> { throw new PlayerRepository.PlayerNotFoundException(playerId) }

        and:
        response.statusCode() == 404

        and:
        response.body().asString() == "Can not find player with id: [${playerId}] in repository."
    }
}
