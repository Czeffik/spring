package com.trzewik.spring.infrastructure.db.game

import com.trzewik.spring.domain.game.Card
import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.PlayerInGame
import com.trzewik.spring.domain.player.Player
import groovy.json.JsonSlurper
import groovy.sql.GroovyRowResult

trait PlayerInGameTableVerification {
    abstract JsonSlurper getSlurper()

    boolean validatePlayerInGame(List<GroovyRowResult> playersInGamesFromDb, Game game, PlayerInGame player) {
        def playerInGameFromDb = playersInGamesFromDb.find { it.player_id == player.playerId && it.game_id == game.id }

        assert playerInGameFromDb
        assert playerInGameFromDb.move == player.move?.name()
        assert validateHand(slurper.parseText(playerInGameFromDb.hand.value), player.hand)

        return true
    }

    boolean validateHand(parsedHand, Set<Card> hand) {
        (parsedHand as List).each { parsedCard ->
            assert hand.collect { it.suit.name() }.contains(parsedCard.suit)
            assert hand.collect { it.rank.name() }.contains(parsedCard.rank)
        }

        return true
    }
}
