package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerCreation

trait GameCreation {

    Game createGame(GameCreator creator = new GameCreator()) {
        return new Game(
            creator.id,
            creator.deck,
            creator.players,
            creator.croupier,
            creator.status
        )
    }

    static class GameCreator implements PlayerCreation, PlayerInGameCreation, DeckCreation, CardCreation {
        String id = UUID.randomUUID().toString()
        Deck deck = createDeck()
        Map<Player, PlayerInGame> players = [(createPlayer(PlayerCreator.croupier())): createPlayerInGame(), (createPlayer()): createPlayerInGame()]
        Player croupier = createPlayer(PlayerCreator.croupier())
        Game.Status status = Game.Status.NOT_STARTED

        GameCreator startedGame() {
            return new GameCreator(
                status: Game.Status.STARTED,
                players: [
                    (createPlayer(PlayerCreator.croupier())): createPlayerInGame(new PlayerInGameCreator(
                        hand: [createCard(new CardCreator(rank: Card.Rank.QUEEN)), createCard(new CardCreator(rank: Card.Rank.JACK))] as Set,
                    )),
                    (createPlayer())                        : createPlayerInGame(new PlayerInGameCreator(
                        hand: [createCard(), createCard(new CardCreator(rank: Card.Rank.KING))] as Set,
                    ))]
            )

        }

        GameCreator() {}

        GameCreator(Game game, Map map) {
            this.id = map.id ?: game.id
            this.deck = map.deck as Deck ?: game.deck
            this.players = map.players as Map<Player, PlayerInGame> ?: game.players
            this.croupier = map.croupier as Player ?: game.croupier
            this.status = map.status as Game.Status ?: game.status
        }

        GameCreator(Game game) {
            this.id = game.id
            this.deck = game.deck
            this.players = game.players
            this.croupier = game.croupier
            this.status = game.status
        }
    }
}
