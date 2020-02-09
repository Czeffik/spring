package com.trzewik.spring.infrastructure.db


import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerCreation
import com.trzewik.spring.domain.player.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles(['test-db'])
@ContextConfiguration(
    classes = [TestDbConfig],
    initializers = [DbInitializer]
)
class PlayerRepositoryIT extends DbSpec implements PlayerCreation {
    @Autowired
    PlayerRepository repository

    def 'should save player to database'() {
        given:
        Player player = createPlayer()

        when:
        repository.save(player)

        then:
        def players = helper.getAllPlayers()
        players.size() == 1
        with(players.first()) {
            id == player.id
            name == player.name
        }
    }

    def 'should find player in database by id'() {
        given:
        Player player = createPlayer()

        and:
        helper.save(player)

        when:
        Optional<Player> found = repository.findById(player.id)

        then:
        found.isPresent()
        with(found.get()) {
            id == player.id
            name == player.name
        }
    }

    def 'should get player from database by id'() {
        given:
        Player player = createPlayer()

        and:
        helper.save(player)

        when:
        Player found = repository.getById(player.id)

        then:
        with(found) {
            id == player.id
            name == player.name
        }
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
        def players = createPlayers(4)
        def additionalPlayer = createPlayer()

        and:
        players.each { helper.save(it) }

        when:
        def found = repository.findAllById(players.collect { it.id })

        then:
        found.size() == players.size()

        and:
        !found.contains(additionalPlayer)

        and:
        found.containsAll(players)
    }
}
