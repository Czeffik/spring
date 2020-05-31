package com.trzewik.spring.infrastructure.db.game


import com.trzewik.spring.domain.game.Deck
import com.trzewik.spring.domain.game.Game
import groovy.json.JsonSlurper
import groovy.sql.GroovyRowResult

trait GameTableVerification {
    abstract JsonSlurper getSlurper()

    boolean validateGame(List<GroovyRowResult> gamesFromDb, Game game) {
        def gameInDb = gamesFromDb.find { it.id == game.id }

        assert gameInDb
        assert gameInDb.status == game.status.name()
        assert gameInDb.croupier_id == game.croupierId
        assert validateDeck(slurper.parseText(gameInDb.deck.value), game.deck)

        return true
    }

    boolean validateDeck(parsedDeck, Deck deck) {
        (parsedDeck.cards as List).eachWithIndex { parsedCard, index ->
            assert parsedCard.suit == deck.cards.get(index).suit.name()
            assert parsedCard.rank == deck.cards.get(index).rank.name()
        }

        return true
    }
}
