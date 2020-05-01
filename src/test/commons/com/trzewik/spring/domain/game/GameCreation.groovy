package com.trzewik.spring.domain.game

trait GameCreation implements GamePlayerCreation, DeckCreation {

    Game createGame(GameBuilder builder = new GameBuilder()) {
        return new Game(
            builder.id,
            builder.players,
            builder.croupierId,
            builder.deck,
            builder.status,
            builder.currentPlayerId
        )
    }

    Game createStartedGame() {
        GamePlayer currPlayer = createGamePlayer(
            new GamePlayerBuilder(hand: [createCard(Rank.FOUR), createCard(Rank.SEVEN)])
        )
        GamePlayer croupier = createGamePlayer(
            new GamePlayerBuilder(hand: [createCard(Rank.EIGHT), createCard(Rank.QUEEN)])
        )
        Game game = createGame(new GameBuilder(
            players: [currPlayer, croupier],
            croupierId: croupier.id,
            currentPlayerId: currPlayer.id,
            status: Status.STARTED
        ))
        return game
    }

    static class GameBuilder implements GameCreation {
        String id = UUID.randomUUID().toString()
        Set<GamePlayer> players = [createGamePlayer()] as Set
        String croupierId = players.first().id
        Deck deck = createDeck()
        Status status = Status.NOT_STARTED
        String currentPlayerId = null

        GameBuilder() {}

        GameBuilder(Game game) {
            id = game.id
            players = game.players
            croupierId = game.croupierId
            deck = game.deck
            status = game.status
            currentPlayerId = game.currentPlayerId
        }
    }
}
