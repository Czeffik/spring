package com.trzewik.spring.infrastructure.db


import com.trzewik.spring.domain.player.PlayerCreation
import com.trzewik.spring.domain.player.PlayerRepository
import com.trzewik.spring.infrastructure.db.player.PlayerTableInteraction
import com.trzewik.spring.infrastructure.db.player.PlayerTableVerification
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared

@Slf4j
@ActiveProfiles(['test-db'])
@ContextConfiguration(
    classes = [TestDbConfig],
    initializers = [DbInitializer]
)
class PlayerRepositoryIT extends DbSpec implements PlayerCreation, PlayerTableInteraction, PlayerTableVerification {
    @Autowired
    PlayerRepository repository
    @Shared
    Sql sql

    def setupSpec() {
        sql = Sql.newInstance(
            container.jdbcUrl,
            container.username,
            container.password
        )
    }

    def setup() {
        deleteAllPlayers()
    }

    def cleanup() {
        deleteAllPlayers()
    }

    def cleanupSpec() {
        sql.close()
    }

    def 'should save player in database'() {
        given:
            def player = createPlayer()
        when:
            repository.save(player)
        then:
            def players = getAllPlayers()
            players.size() == 1
        and:
            validatePlayer(players, player)
    }

    def 'should find player in database by id'() {
        given:
            def player = createPlayer()
        and:
            savePlayer(player)
        when:
            def found = repository.findById(player.id)
        then:
            found.isPresent()
        and:
            found.get() == player
    }

    def 'should get player from database by id'() {
        given:
            def player = createPlayer()
        and:
            savePlayer(player)
        when:
            def found = repository.getById(player.id)
        then:
            found == player
    }

    def 'should throw exception when can not get player by id'() {
        given:
            def playerId = 'player-id'
        when:
            repository.getById(playerId)
        then:
            def ex = thrown(PlayerRepository.PlayerNotFoundException)
        and:
            ex.message == "Can not find player with id: [${playerId}] in repository."
    }

    def 'should get empty list when can not find players with ids in repository'() {
        expect:
            repository.findAllById(['a', 'b', 'c', 'd']).isEmpty()
    }

    def 'should get list with players by id from repository'() {
        given:
            def players = createPlayers()
        and:
            def additionalPlayer = createPlayer()
        and:
            players.each { savePlayer(it) }
        and:
            savePlayer(additionalPlayer)
        when:
            def found = repository.findAllById(players.collect { it.id })
        then:
            found.size() == players.size()
        and:
            !found.contains(additionalPlayer)
        and:
            found.containsAll(players)
    }

    @Override
    String getDefaultSchema() {
        return DEFAULT_SCHEMA
    }
}
