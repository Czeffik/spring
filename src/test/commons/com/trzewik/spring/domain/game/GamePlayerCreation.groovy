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
        return new GamePlayer(
            builder.player,
            builder.hand,
            builder.move
        )
    }

    static class GamePlayerBuilder implements GamePlayerCreation {
        Player player = createPlayer()
        Set<Card> hand = [] as Set
        Move move = Move.HIT

        GamePlayerBuilder() {}

        GamePlayerBuilder(GamePlayer player) {
            this.player = createPlayer(new PlayerBuilder(player))
            this.hand = player.hand
            this.move = player.move
        }
    }

}
