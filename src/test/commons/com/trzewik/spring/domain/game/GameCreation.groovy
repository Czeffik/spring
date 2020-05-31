package com.trzewik.spring.domain.game

trait GameCreation {

    Game createGame(GameCreator creator = new GameCreator()) {
        return new Game(
            creator.id,
            creator.deck,
            creator.players,
            creator.croupierId,
            creator.status
        )
    }

    static class GameCreator implements PlayerInGameCreation, DeckCreation, CardCreation {
        String id = UUID.randomUUID().toString()
        Deck deck = createDeck()
        List<PlayerInGame> players = [createPlayerInGame(), createPlayerInGame()]
        String croupierId = players.first().playerId
        Game.Status status = Game.Status.NOT_STARTED

        GameCreator startedGame() {
            def croupier = createPlayerInGame(new PlayerInGameCreator(
                hand: [createCard(new CardCreator(rank: Card.Rank.QUEEN)), createCard(new CardCreator(rank: Card.Rank.JACK))] as Set,
            ))
            return new GameCreator(
                status: Game.Status.STARTED,
                players: [
                    croupier,
                    createPlayerInGame(new PlayerInGameCreator(
                        hand: [createCard(), createCard(new CardCreator(rank: Card.Rank.KING))] as Set,
                    ))],
                croupierId: croupier.playerId
            )
        }

        GameCreator() {}

        GameCreator(Game game, Map map) {
            this.id = map.id ?: game.id
            this.deck = map.deck as Deck ?: game.deck
            this.players = map.players as List<PlayerInGame> ?: game.players
            this.croupierId = map.croupier as String ?: game.croupierId
            this.status = map.status as Game.Status ?: game.status
        }

        GameCreator(Game game) {
            this.id = game.id
            this.deck = game.deck
            this.players = game.players
            this.croupierId = game.croupierId
            this.status = game.status
        }
    }
}
