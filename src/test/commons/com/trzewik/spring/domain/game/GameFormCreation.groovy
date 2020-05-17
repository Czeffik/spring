package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.player.Player

trait GameFormCreation {

    GameService.MoveForm createMoveForm(MoveFormCreator creator = new MoveFormCreator()) {
        return new GameService.MoveForm(
            playerId: creator.playerId,
            move: creator.move
        )
    }

    GameService.AddPlayerForm createAddPlayerForm(AddPlayerFormCreator creator = new AddPlayerFormCreator()) {
        return new GameService.AddPlayerForm(
            playerId: creator.playerId
        )
    }

    static class MoveFormCreator {
        String playerId = 'example-player-id'
        Game.Move move = Game.Move.STAND

        MoveFormCreator() {}

        MoveFormCreator(Player player, Game.Move move) {
            this.playerId = player.id
            this.move = move
        }
    }

    static class AddPlayerFormCreator {
        String playerId = 'example-player-id'
    }
}
