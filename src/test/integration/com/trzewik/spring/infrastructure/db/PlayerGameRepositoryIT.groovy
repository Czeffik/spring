package com.trzewik.spring.infrastructure.db

import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.GameCreation
import com.trzewik.spring.domain.game.PlayerGameRepository
import com.trzewik.spring.domain.player.Player
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

import javax.persistence.PersistenceException

@ActiveProfiles(['test-db'])
@ContextConfiguration(
    classes = [TestDbConfig],
    initializers = [DbInitializer]
)
class PlayerGameRepositoryIT extends DbSpec implements GameCreation {
    @Autowired
    PlayerGameRepository repository

    def 'should save player in existing game'() {
        given:
        Game game = createGame()
        Player croupier = game.croupier

        and:
        helper.save(game)

        and:
        helper.save(croupier)

        when:
        repository.save(croupier, game.id)

        then:
        def playerGames = helper.getAllPlayerGames()
        playerGames.size() == 1
        with(playerGames.first()) {
            game_id == game.id
            player_id == croupier.id
//            hand == croupier.hand todo
            move == croupier.move.name()
        }
    }

    def 'should not be able add player to game when game does not exist in database'() {
        given:
        Game game = createGame()
        Player croupier = game.croupier

        and:
        helper.save(croupier)

        when:
        repository.save(croupier, game.id)

        then:
        thrown(PersistenceException)
    }

    def 'should not be able add player to game when player does not exist in database'() {
        given:
        Game game = createGame()
        Player croupier = game.croupier

        and:
        helper.save(game)

        when:
        repository.save(croupier, game.id)

        then:
        thrown(PersistenceException)
    }

    def 'should find player with game'() {
        given:
        Game game = createGame()
        Player croupier = game.croupier

        and:
        helper.save(game)

        and:
        helper.save(croupier)

        and:
        helper.save(game.id, croupier)

        when:
        Optional<Player> found = repository.findById(game.id, croupier.id)

        then:
        found.isPresent()
        with(found.get()) {
            id == croupier.id
            name == croupier.name
//            hand == croupier.hand todo
            move == croupier.move
        }
    }

    def 'should update player in game'() {
        given:
        Game game = createGame()
        Player croupier = game.croupier

        and:
        helper.save(game)

        and:
        helper.save(croupier)

        and:
        helper.save(game.id, croupier)

        and:
        Player updated = createPlayer(new PlayerBuilder(
            id: croupier.id,
            name: 'new-croupier-name',
            move: Game.Move.STAND,
            hand: []
        ))

        when:
        repository.update(updated, game.id)

        then:
        def playerGames = helper.getAllPlayerGames()
        playerGames.size() == 1

        and:
        with(playerGames.first()) {
            player_id == croupier.id
            game_id == game.id
            move == updated.move.name()
//            hand == updated.hand  todo
        }

        and:
        def players = helper.getAllPlayers()
        players.size() == 1

        and: 'name should not change'
        with(players.first()) {
            id == croupier.id
            name == croupier.name
        }
    }

    def 'should update players in game'() {
        given:
        Game game = createGame()
        Player croupier = game.croupier
        Player player = game.currentPlayer
        def players = [croupier, player]

        and:
        helper.save(game)

        and:
        players.each { helper.save(it) }

        and:
        players.each { helper.save(game.id, it) }

        and:
        Player updatedCroupier = createPlayer(new PlayerBuilder(
            id: croupier.id,
            name: 'new-croupier-name',
            move: Game.Move.STAND,
            hand: []
        ))

        and:
        Player updatedPlayer = createPlayer(new PlayerBuilder(
            id: player.id,
            name: 'new-player-name',
            move: Game.Move.STAND,
            hand: []
        ))
        and:
        def updatedPlayers = [updatedCroupier, updatedPlayer]

        when:
        repository.update(updatedPlayers, game.id)

        then:
        def playerGames = helper.getAllPlayerGames()
        playerGames.size() == 2

        and:
        playerGames.find{it.player_id}
        with(playerGames.find{it.player_id == croupier.id}) {
            game_id == game.id
            move == updatedCroupier.move.name()
//            hand == updatedCroupier.hand  todo
        }

        and:
        playerGames.find{it.player_id}
        with(playerGames.find{it.player_id == player.id}) {
            game_id == game.id
            move == updatedPlayer.move.name()
//            hand == updatedCroupier.hand  todo
        }

    }
}
