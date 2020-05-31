package com.trzewik.spring.infrastructure.application.game

import com.trzewik.spring.domain.game.PlayerServiceClient
import com.trzewik.spring.domain.player.PlayerCreation
import com.trzewik.spring.domain.player.PlayerRepository
import com.trzewik.spring.domain.player.PlayerService
import com.trzewik.spring.infrastructure.application.TestApplicationConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ActiveProfiles(['test', TestApplicationConfig.PROFILE])
@ContextConfiguration(
    classes = [TestApplicationConfig]
)
class PlayerServiceClientIT extends Specification implements PlayerCreation {
    @Autowired
    PlayerService playerServiceMock

    @Autowired
    PlayerServiceClient playerServiceClient

    def 'should get croupier using player service'() {
        given:
            def croupier = createPlayer(PlayerCreator.croupier())
        when:
            def returned = playerServiceClient.getCroupier()
        then:
            1 * playerServiceMock.getCroupier(_ as PlayerService.GetCroupierCommand) >> croupier
        and:
            returned.playerId == croupier.id
            returned.name == croupier.name
            returned.move == null
            returned.hand.isEmpty()
    }

    def 'should get player with id using player service'() {
        given:
            def player = createPlayer()
        when:
            def returned = playerServiceClient.getPlayer(player.id)
        then:
            1 * playerServiceMock.get({ PlayerService.GetPlayerCommand command ->
                assert command.playerId == player.id
            }) >> player
        and:
            returned.playerId == player.id
            returned.name == player.name
            returned.move == null
            returned.hand.isEmpty()
    }

    def 'should throw exception when player not found by player service'() {
        given:
            def playerId = 'example id'
        when:
            playerServiceClient.getPlayer(playerId)
        then:
            1 * playerServiceMock.get({ PlayerService.GetPlayerCommand command ->
                assert command.playerId == playerId
            }) >> { throw new PlayerRepository.PlayerNotFoundException(playerId) }
        and:
            PlayerServiceClient.PlayerNotFoundException ex = thrown()
            ex.message == "com.trzewik.spring.domain.player.PlayerRepository\$PlayerNotFoundException: Can not find player with id: [$playerId] in repository."
    }
}
