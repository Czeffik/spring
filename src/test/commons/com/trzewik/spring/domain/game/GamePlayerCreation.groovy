package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.common.Deck

trait GamePlayerCreation {

    Set<GamePlayer> createGamePlayers(int numberOfPlayers = 2) {
        def players = []
        2.times { players << createGamePlayer() }
        return players
    }

    GamePlayer createGamePlayer(GamePlayerBuilder builder = new GamePlayerBuilder()) {
        return new GamePlayerImpl(
            builder.playerId,
            builder.hand,
            builder.move
        )
    }

    static class GamePlayerBuilder implements GamePlayerCreation {
        String playerId = UUID.randomUUID().toString()
        Set<Deck.Card> hand = [] as Set
        Game.Move move = Game.Move.HIT

        GamePlayerBuilder() {}

        GamePlayerBuilder(GamePlayer player) {
            playerId = player.playerId
            hand = player.hand
            move = player.move
        }
    }

}
