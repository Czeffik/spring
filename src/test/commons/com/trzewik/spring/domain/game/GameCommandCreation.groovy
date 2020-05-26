package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.player.Player

trait GameCommandCreation {

    GameService.MoveCommand createMoveCommand(MoveCommandCreator creator = new MoveCommandCreator()) {
        return new GameService.MoveCommand(
            creator.gameId,
            creator.playerId,
            creator.move
        )
    }

    GameService.AddPlayerCommand createAddPlayerCommand(AddPlayerCommandCreator creator = new AddPlayerCommandCreator()) {
        return new GameService.AddPlayerCommand(
            creator.gameId,
            creator.playerId
        )
    }

    static class MoveCommandCreator {
        String gameId = 'example-game-id'
        String playerId = 'example-player-id'
        Game.Move move = Game.Move.STAND

        MoveCommandCreator() {}

        MoveCommandCreator(Game game, Player player, Game.Move move) {
            this.gameId = game.id
            this.playerId = player.id
            this.move = move
        }
    }

    static class AddPlayerCommandCreator {
        String gameId = 'example-game-id'
        String playerId = 'example-player-id'

        AddPlayerCommandCreator() {}

        AddPlayerCommandCreator(Game game, Player player) {
            this.gameId = game.id
            this.playerId = player.id
        }
    }
}
