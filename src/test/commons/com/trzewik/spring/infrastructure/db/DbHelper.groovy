package com.trzewik.spring.infrastructure.db

import com.trzewik.spring.domain.game.Deck
import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.GamePlayer
import com.trzewik.spring.domain.player.Player
import groovy.json.JsonBuilder
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import org.testcontainers.containers.PostgreSQLContainer

@Slf4j
class DbHelper {
    private Sql sql
    private static String defaultSchema

    DbHelper(PostgreSQLContainer container, String defaultSchema) {
        sql = Sql.newInstance(
            container.jdbcUrl,
            container.username,
            container.password
        )
        this.defaultSchema = defaultSchema
    }

    List<GroovyRowResult> getAllGames() {
        String query = "SELECT * FROM $gamesTable"
        log.info(query)
        sql.rows(query)
    }

    void deleteGames() {
        String query = "DELETE FROM $gamesTable"
        log.info(query)
        sql.execute(query)
    }

    List<List<Object>> save(Game game) {
        String query = "INSERT INTO $gamesTable (id, deck, status, current_player_id, croupier_id) VALUES (?, CAST(? AS JSON), ?, ?, ?)"
        log.info(query)
        sql.executeInsert(query.toString(), [game.id, convertDeck(game.deck), game.status.name(), game.currentPlayerId, game.croupierId])
    }

    static String convertDeck(Deck deck) {
        def cards = convertCards(deck.cards)
        return new JsonBuilder([cards: cards]).toPrettyString()
    }

    List<GroovyRowResult> getAllPlayers() {
        String query = "SELECT * FROM $playersTable"
        log.info(query)
        sql.rows(query)
    }

    void deletePlayers() {
        String query = "DELETE FROM $playersTable"
        log.info(query)
        sql.execute(query)
    }

    List<List<Object>> save(Player player) {
        String query = "INSERT INTO $playersTable (id, name) VALUES (?, ?)"
        log.info(query)
        sql.executeInsert(query.toString(), [player.id, player.name])
    }

    List<GroovyRowResult> getAllGamesPlayers() {
        String query = "SELECT * FROM $gamesPlayersTable"
        log.info(query)
        sql.rows(query)
    }

    void deleteGamesPlayers() {
        String query = "DELETE FROM $gamesPlayersTable"
        log.info(query)
        sql.execute(query)
    }

    List<List<Object>> save(String gameId, GamePlayer player) {
        String query = "INSERT INTO $gamesPlayersTable (game_id, player_id, hand, move) VALUES (?, ?, CAST(? AS JSON), ?)"
        log.info(query)
        sql.executeInsert(query.toString(), [gameId, player.player.id, convertCardsToString(player.hand), player.move.name()])
    }

    static String convertCardsToString(Collection<Deck.Card> hand) {
        return new JsonBuilder(convertCards(hand)).toPrettyString()
    }

    static List<Map> convertCards(Collection<Deck.Card> hand) {
        return hand.collect { [suit: it.suit, rank: it.rank] }
    }

    private static String getGamesTable() {
        return table('games')
    }

    private static String getPlayersTable() {
        return table('players')
    }

    private static String getGamesPlayersTable() {
        return table('games_players')
    }

    private static String table(String name) {
        return "$defaultSchema.$name".toString()
    }
}
