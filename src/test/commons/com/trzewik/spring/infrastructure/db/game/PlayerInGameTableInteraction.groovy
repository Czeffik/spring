package com.trzewik.spring.infrastructure.db.game

import com.trzewik.spring.domain.game.Card
import com.trzewik.spring.domain.game.PlayerInGame
import com.trzewik.spring.infrastructure.db.DatabaseTableInteraction
import groovy.json.JsonBuilder
import groovy.sql.GroovyRowResult
import groovy.util.logging.Slf4j

@Slf4j
trait PlayerInGameTableInteraction extends DatabaseTableInteraction {

    List<GroovyRowResult> getAllPlayersInGame() {
        return getAll(gamesPlayersTable)
    }

    void deleteAllPlayersInGame() {
        deleteAll(gamesPlayersTable)
    }

    List<List<Object>> savePlayerInGame(String gameId, PlayerInGame player) {
        String query = "INSERT INTO $gamesPlayersTable (game_id, player_id, hand, move) VALUES (?, ?, CAST(? AS JSON), ?)"
        log.info(query)
        sql.executeInsert(query.toString(), [gameId, player.playerId, convertCardsToString(player.hand), player.move?.name()])
    }

    String convertCardsToString(Collection<Card> hand) {
        return new JsonBuilder(convertCards(hand)).toPrettyString()
    }

    List<Map> convertCards(Collection<Card> hand) {
        return hand.collect { [suit: it.suit, rank: it.rank] }
    }

    String getGamesPlayersTable() {
        return table('games_players')
    }
}
