package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerCreation

trait GamePlayerCreation implements PlayerCreation {

    Set<GamePlayer> createGamePlayers(int numberOfPlayers = 2) {
        def players = []
        2.times { players << createGamePlayer() }
        return players
    }

    GamePlayer createGamePlayer(GamePlayerBuilder builder = new GamePlayerBuilder()) {
        return new GamePlayerImpl(
            builder.player,
            builder.hand,
            builder.move
        )
    }

    static class GamePlayerBuilder implements GamePlayerCreation {
        Player player = createPlayer()
        Set<Deck.Card> hand = [] as Set
        Game.Move move = Game.Move.HIT

        GamePlayerBuilder() {}

        GamePlayerBuilder(GamePlayer player) {
            this.player = player.player
            this.hand = player.hand
            this.move = player.move
        }
    }

}
