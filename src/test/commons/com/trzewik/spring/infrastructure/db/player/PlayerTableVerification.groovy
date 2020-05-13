package com.trzewik.spring.infrastructure.db.player

import com.trzewik.spring.domain.player.Player
import groovy.sql.GroovyRowResult

trait PlayerTableVerification {

    boolean validatePlayer(List<GroovyRowResult> playersFromDb, Player player) {
        def playerInDb = playersFromDb.find { it.id == player.id }

        assert playerInDb
        assert playerInDb.name == player.name

        return true
    }
}
