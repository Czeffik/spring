package com.trzewik.spring.infrastructure.db.game

import com.trzewik.spring.domain.game.Card
import com.trzewik.spring.domain.game.Deck
import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.infrastructure.db.DatabaseTableInteraction
import groovy.json.JsonBuilder
import groovy.sql.GroovyRowResult
import groovy.util.logging.Slf4j

@Slf4j
trait GameTableInteraction extends DatabaseTableInteraction {

    List<GroovyRowResult> getAllGames() {
        return getAll(gamesTable)
    }

    void deleteAllGames() {
        deleteAll(gamesTable)
    }

    List<List<Object>> saveGame(Game game) {
        String query = "INSERT INTO $gamesTable (id, deck, status, croupier_id) VALUES (?, CAST(? AS JSON), ?, ?)"
        log.info(query)
        sql.executeInsert(query.toString(), [game.id, convertDeck(game.deck), game.status.name(), game.croupierId])
    }

    String convertDeck(Deck deck) {
        def cards = convertCards(deck.cards)
        return new JsonBuilder([cards: cards]).toPrettyString()
    }

    List<Map> convertCards(Collection<Card> hand) {
        return hand.collect { [suit: it.suit, rank: it.rank] }
    }

    String getGamesTable() {
        return table('games')
    }
}
