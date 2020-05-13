package com.trzewik.spring.infrastructure.db


import com.trzewik.spring.domain.game.CardCreation
import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.GameCreation
import com.trzewik.spring.domain.game.GameRepository
import com.trzewik.spring.domain.game.PlayerInGameCreation
import com.trzewik.spring.domain.player.PlayerCreation
import com.trzewik.spring.infrastructure.db.game.GameTableInteraction
import com.trzewik.spring.infrastructure.db.game.GameTableVerification
import com.trzewik.spring.infrastructure.db.game.PlayerInGameTableInteraction
import com.trzewik.spring.infrastructure.db.game.PlayerInGameTableVerification
import com.trzewik.spring.infrastructure.db.player.PlayerTableInteraction
import com.trzewik.spring.infrastructure.db.player.PlayerTableVerification
import groovy.json.JsonSlurper
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared

@Slf4j
@ActiveProfiles(['test-db'])
@ContextConfiguration(
    classes = [TestDbConfig],
    initializers = [DbInitializer]
)
class GameRepositoryIT extends DbSpec implements GameCreation, PlayerInGameCreation, PlayerCreation, CardCreation,
    GameTableInteraction, PlayerInGameTableInteraction, PlayerTableInteraction,
    GameTableVerification, PlayerInGameTableVerification, PlayerTableVerification {

    @Autowired
    GameRepository repository

    @Shared
    Sql sql
    @Shared
    JsonSlurper jsonSlurper = new JsonSlurper()

    def setupSpec() {
        sql = Sql.newInstance(
            container.jdbcUrl,
            container.username,
            container.password
        )
    }

    def setup() {
        deleteAllPlayersInGame()
        deleteAllPlayers()
        deleteAllGames()
    }

    def cleanup() {
        deleteAllPlayersInGame()
        deleteAllPlayers()
        deleteAllGames()
    }

    def cleanupSpec() {
        sql.close()
    }

    def 'should save game in database'() {
        given:
            def game = createGame(new GameCreator().startedGame())
        and:
            game.players.each { player, playerInGame -> savePlayer(player) }
        when:
            repository.save(game)
        then:
            def games = getAllGames()
            games.size() == 1
        and:
            validateGame(games, game)
        and:
            def playersInGames = getAllPlayersInGame()
            playersInGames.size() == 2
        and:
            validatePlayerInGame(playersInGames, game, game.croupier, game.players)
        and:
            validatePlayerInGame(playersInGames, game, game.currentPlayer, game.players)
    }

    def '''should return empty optional when can not find game with it
        and should return optional with game when can find game with id'''() {
        given:
            def game = createGame(new GameCreator().startedGame())
        and:
            game.players.each { player, playerInGame -> savePlayer(player) }
        and:
            saveGame(game)
        and:
            game.players.each { player, playerInGame -> savePlayerInGame(game.id, player.id, playerInGame) }
        expect:
            !repository.findById('other-id').isPresent()
        when:
            def foundGame = repository.findById(game.id)
        then:
            foundGame.present
            foundGame.get() == game
    }

    def '''should throw exception when can not get game by id (not present id repository)
        and should return game when game with id is present in repository'''() {
        given:
            def game = createGame(new GameCreator().startedGame())
        and:
            game.players.each { player, playerInGame -> savePlayer(player) }
        and:
            saveGame(game)
        and:
            game.players.each { player, playerInGame -> savePlayerInGame(game.id, player.id, playerInGame) }
        when:
            repository.getById('other-id')
        then:
            def ex = thrown(GameRepository.GameNotFoundException)
        and:
            ex.message == 'Game with id: [other-id] not found.'
        when:
            def foundGame = repository.getById(game.id)
        then:
            foundGame == game
    }

    def 'should update game in database'() {
        given:
            def game = createGame(new GameCreator().startedGame())
        and:
            game.players.each { player, playerInGame -> savePlayer(player) }
        and:
            saveGame(game)
        and:
            game.players.each { player, playerInGame -> savePlayerInGame(game.id, player.id, playerInGame) }
        and:
            def updatedGame = createGame(new GameCreator(
                game,
                [
                    status : Game.Status.ENDED,
                    players: game.players.collectEntries { player, playerInGame ->
                        [player, createPlayerInGame(new PlayerInGameCreator(playerInGame, [move: Game.Move.STAND]))]
                    }
                ]
            ))
        when:
            repository.update(updatedGame)
        then:
            def games = getAllGames()
            games.size() == 1
        and:
            validateGame(games, updatedGame)
        and:
            def playersInGames = getAllPlayersInGame()
            playersInGames.size() == 2
        and:
            validatePlayerInGame(playersInGames, game, game.croupier, game.players)
        and:
            validatePlayerInGame(playersInGames, game, game.currentPlayer, game.players)
    }

    def 'should throw exception when missing record in player table'() {
        when:
            repository.save(createGame(new GameCreator().startedGame()))
        then:
            DataIntegrityViolationException ex = thrown()
            ex.message == ''
    }

    @Override
    String getDefaultSchema() {
        return DEFAULT_SCHEMA
    }

    @Override
    JsonSlurper getSlurper() {
        return jsonSlurper
    }
}
