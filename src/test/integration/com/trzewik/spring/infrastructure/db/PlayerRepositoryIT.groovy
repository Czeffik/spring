package com.trzewik.spring.infrastructure.db

import com.trzewik.spring.domain.game.GameCreation
import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles(['test-db'])
@ContextConfiguration(
    classes = [TestDbConfig],
    initializers = [DbInitializer]
)
class PlayerRepositoryIT extends DbSpec implements GameCreation {
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
}
