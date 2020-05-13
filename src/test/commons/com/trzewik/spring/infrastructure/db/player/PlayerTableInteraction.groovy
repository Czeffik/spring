package com.trzewik.spring.infrastructure.db.player

import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.infrastructure.db.DatabaseTableInteraction
import groovy.sql.GroovyRowResult
import groovy.util.logging.Slf4j

@Slf4j
trait PlayerTableInteraction extends DatabaseTableInteraction {

    List<GroovyRowResult> getAllPlayers() {
        return getAll(playersTable)
    }

    void deleteAllPlayers() {
        deleteAll(playersTable)
    }

    List<List<Object>> savePlayer(Player player) {
        String query = "INSERT INTO $playersTable (id, name) VALUES (?, ?)"
        log.info(query)
        return sql.executeInsert(query.toString(), [player.id, player.name])
    }

    String getPlayersTable() {
        return table('players')
    }
}
