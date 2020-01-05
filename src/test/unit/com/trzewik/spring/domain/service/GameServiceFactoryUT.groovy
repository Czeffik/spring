package com.trzewik.spring.domain.service

import com.trzewik.spring.domain.game.GameRepository
import com.trzewik.spring.domain.game.PlayerGameRepository
import com.trzewik.spring.domain.player.PlayerRepository
import spock.lang.Specification

class GameServiceFactoryUT extends Specification {
    def 'should create game service with given repositories'() {
        given:
        def game = Mock(GameRepository)
        def player = Mock(PlayerRepository)
        def playerGame = Mock(PlayerGameRepository)

        when:
        def service = GameServiceFactory.create(game, player, playerGame)

        then:
        service.@gameRepo == game
        service.@playerRepo == player
        service.@playerGameRepo == playerGame
    }
}
