package com.trzewik.spring.infrastructure.db

import com.trzewik.spring.domain.deck.Deck
import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.GameCreation
import com.trzewik.spring.domain.game.GameRepository
import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.infrastructure.db.game.GameDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles(['test-db'])
@ContextConfiguration(
    classes = [TestDbConfig],
    initializers = [DbInitializer]
)
class GameRepositoryIT extends DbSpec implements GameCreation {

    @Autowired
    GameRepository repository

    def 'should save game in database'() {
        given:
        Game game = createGame()

        when:
        repository.save(game)

        then:
        def games = helper.getAllGames()
        games.size() == 1

        and:
        with(games.first()) {
            id == game.id
            status == game.status.name()
            current_player_id == game?.currentPlayer?.id
            croupier_id == game.croupier.id
        }
        def parsedDeck = slurper.parseText(games.first().deck.value)
        validateDeck(game.deck, parsedDeck)

        and:
        helper.getAllPlayerGames().size() == 0
        helper.getAllPlayers().size() == 0
    }

    def 'should find game by id in database and pack it in optional'() {
        given:
        Player croupier = createPlayer()

        and:
        helper.save(croupier)

        and:
        Game game = createGame(new GameBuilder(
            currentPlayer: null,
            croupier: croupier,
            players: [],
            status: Game.Status.NOT_STARTED
        ))

        and:
        helper.save(game)

        and:
        helper.save(game.id, croupier)

        expect:
        !repository.findById('other-id').isPresent()

        and:
        Optional<Game> foundGame = repository.findById(game.id)
        foundGame.present
        foundGame.get() == game
    }

    def 'should find game by id in database'() {
        given:
        Player croupier = createPlayer()

        and:
        helper.save(croupier)

        and:
        Game game = createGame(new GameBuilder(
            currentPlayer: null,
            croupier: croupier,
            players: [],
            status: Game.Status.NOT_STARTED
        ))

        and:
        helper.save(game)

        and:
        helper.save(game.id, croupier)

        when:
        repository.findGame('other-id')

        then:
        thrown(GameRepository.GameNotFoundException)

        when:
        Game foundGame = repository.findGame(game.id)

        then:
        foundGame == game
    }

    def 'should update game in database'() {
        given:
        Game game = createGame()

        and:
        helper.save(game)

        and:
        Game updated = createGame(new GameBuilder(
            id: game.id,
            deck: createDeck(new DeckBuilder(cards: [createCard(), createCard(Deck.Card.Rank.FOUR)] as Stack)),
            status: Game.Status.ENDED,
            croupier: createPlayer(new PlayerBuilder(id: 'croupier-new-id')),
            currentPlayer: createPlayer(new PlayerBuilder(id: 'current-player-new-id')),
            players: []
        ))

        when:
        repository.update(updated)

        then:
        def games = helper.getAllGames()
        games.size() == 1
        with(games.first()) {
            id == updated.id
            status == updated.status.name()
            current_player_id == updated.currentPlayer.id
            croupier_id == updated.croupier.id
        }
        def parsedDeck = slurper.parseText(games.first().deck.value)
        validateDeck(updated.deck, parsedDeck)
    }

    def 'should throw exception when missing record in player table'() {
        given:
        helper.save(GAME)

        when:
        repository.findById(GAME.id)

        then:
        thrown(GameDto.PlayerNotFoundException)

        where:
        GAME << [createGame(new GameBuilder(
            currentPlayer: null,
            croupier: createPlayer(),
            players: [],
            status: Game.Status.NOT_STARTED
        ))]
    }

    void validateDeck(Deck deck, parsedDeck) {
        parsedDeck.cards.each { parsedCard ->
            assert deck.cards.any { it.equals(createCard(new CardBuilder(parsedCard.suit, parsedCard.rank))) }
        }
        assert parsedDeck.cards.size() == deck.cards.size()
    }
}
