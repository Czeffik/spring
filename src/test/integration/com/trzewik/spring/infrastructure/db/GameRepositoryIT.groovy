package com.trzewik.spring.infrastructure.db

import com.trzewik.spring.domain.game.Card
import com.trzewik.spring.domain.game.CardCreation
import com.trzewik.spring.domain.game.Deck
import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.GameCreation
import com.trzewik.spring.domain.game.GameRepository
import com.trzewik.spring.domain.player.PlayerCreation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles(['test-db'])
@ContextConfiguration(
    classes = [TestDbConfig],
    initializers = [DbInitializer]
)
class GameRepositoryIT extends DbSpec implements GameCreation, PlayerCreation, CardCreation {

    @Autowired
    GameRepository repository

    def 'should save game in database'() {
        given:
            Game game = createStartedGame()

        and:
            game.players.each { helper.save(createPlayer(new PlayerCreator(id: it.id))) }

        when:
            repository.save(game)

        then:
            def games = helper.getAllGames()
            games.size() == 1

        and:
            with(games.first()) {
                id == game.id
                status == game.status.name()
                current_player_id == game.currentPlayerId
                croupier_id == game.croupierId
            }
            def parsedDeck = slurper.parseText(games.first().deck.value)
            validateDeck(game.deck, parsedDeck)

        and:
            def gamesPlayers = helper.getAllGamesPlayers()
            gamesPlayers.size() == 2

        and:
            def gameCroupier = gamesPlayers.find { it.player_id == game.croupierId }
            gameCroupier.move == game.croupier.move.name()
            def parsedCroupierHand = slurper.parseText(gameCroupier.hand.value) as List
            validateHand(game.croupier.hand, parsedCroupierHand)

        and:
            def gamePlayer = gamesPlayers.find { it.player_id == game.currentPlayerId }
            gamePlayer.move == game.currentPlayer.move.name()
            def parsedPlayerHand = slurper.parseText(gamePlayer.hand.value) as List
            validateHand(game.currentPlayer.hand, parsedPlayerHand)
    }

    def '''should return empty optional when can not find game with it
        and should return optional with game when can find game with id'''() {
        given:
            Game game = createStartedGame()

        and:
            game.players.each { helper.save(createPlayer(new PlayerCreator(id: it.id))) }

        and:
            helper.save(game)

        and:
            game.players.each { helper.save(game.id, it) }

        expect:
            !repository.findById('other-id').isPresent()

        when:
            Optional<Game> foundGame = repository.findById(game.id)

        then:
            foundGame.present
            foundGame.get() == game
    }

    def '''should throw exception when can not get game by id (not present id repository)
        and should return game when game with id is present in repository'''() {
        given:
            Game game = createStartedGame()

        and:
            game.players.each { helper.save(createPlayer(new PlayerCreator(id: it.id))) }

        and:
            helper.save(game)

        and:
            game.players.each { helper.save(game.id, it) }

        when:
            repository.getById('other-id')

        then:
            def ex = thrown(GameRepository.GameNotFoundException)

        and:
            ex.message == 'Game with id: [other-id] not found.'

        when:
            Game foundGame = repository.getById(game.id)

        then:
            foundGame == game
    }

    def 'should update game in database'() {
        given:
            Game game = createStartedGame()

        and:
            game.players.each { helper.save(createPlayer(new PlayerCreator(id: it.id))) }

        and:
            helper.save(game)

        and:
            game.players.each { helper.save(game.id, it) }

        and:
            def playerId = game.currentPlayerId

        and:
            game.auction(playerId, Game.Move.STAND)

        when:
            repository.update(game)

        then:
            def games = helper.getAllGames()
            games.size() == 1

        and:
            with(games.first()) {
                id == game.id
                status == game.status.name()
                current_player_id == game.currentPlayerId
                croupier_id == game.croupierId
            }
            def parsedDeck = slurper.parseText(games.first().deck.value)
            validateDeck(game.deck, parsedDeck)

        and:
            def gamesPlayers = helper.getAllGamesPlayers()
            gamesPlayers.size() == 2

        and:
            def gameCroupier = gamesPlayers.find { it.player_id == game.croupierId }
            gameCroupier.move == game.croupier.move.name()
            def parsedCroupierHand = slurper.parseText(gameCroupier.hand.value) as List
            validateHand(game.croupier.hand, parsedCroupierHand)

        and:
            def gamePlayer = gamesPlayers.find { it.player_id == playerId }
            def player = game.players.find { it.id == playerId }
            gamePlayer.move == player.move.name()
            def parsedPlayerHand = slurper.parseText(gamePlayer.hand.value) as List
            validateHand(player.hand, parsedPlayerHand)
    }

    def 'should throw exception when missing record in player table'() {
        when:
            repository.save(createStartedGame())

        then:
            thrown(DataIntegrityViolationException)
    }

    void validateDeck(Deck deck, parsedDeck) {
        parsedDeck.cards.each { parsedCard ->
            assert deck.cards.any { it.equals(createCard(new CardCreator(parsedCard.suit, parsedCard.rank))) }
        }
        assert parsedDeck.cards.size() == deck.cards.size()
    }

    void validateHand(Set<Card> hand, List parsedHand) {
        parsedHand.each { parsedCard ->
            assert hand.any { it.equals(createCard(new CardCreator(parsedCard.suit, parsedCard.rank))) }
        }
        assert parsedHand.size() == hand.size()
    }
}
