package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.deck.Deck
import com.trzewik.spring.domain.deck.DeckCreation
import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerCreation

trait GameCreation implements PlayerCreation, DeckCreation {

    Game createGame(GameBuilder builder = new GameBuilder()) {
        return new GameImpl(
            builder.id,
            builder.players,
            builder.croupier,
            builder.deck,
            builder.status,
            builder.currentPlayer
        )
    }

    static class GameBuilder implements GameCreation {
        String id = '123'
        List<Player> players = createPlayers(2)
        Player croupier = createPlayer(new PlayerBuilder(
            hand: [createCard(Deck.Card.Rank.FOUR), createCard(Deck.Card.Rank.SEVEN)] as Set)
        )
        Deck deck = createDeck()
        Game.Status status = Game.Status.STARTED
        Player currentPlayer = players.first()

        GameBuilder() {}

        GameBuilder(Game game) {
            id = game.id
            players = game.players
            croupier = game.croupier
            deck = game.deck
            status = game.status
            currentPlayer = game.currentPlayer
        }

        GameBuilder(List<PlayerBuilder> playerBuilders) {
            players = playerBuilders.collect { createPlayer(it) }
        }
    }
}
