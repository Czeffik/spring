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

    static class GameCreator implements PlayerCreation, PlayerInGameCreation, CardCreation {
        String id = UUID.randomUUID().toString()
        Deck deck = new Deck()
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

        GameCreator(Game game) {
            id = game.id
            deck = game.deck
            players = game.players
            croupier = game.croupier
            status = game.status
        }
    }
}
