package com.trzewik.spring.domain.game

trait PlayerInGameCreation {
    PlayerInGame createPlayerInGame(PlayerInGameCreator creator = new PlayerInGameCreator()) {
        return new PlayerInGame(
            creator.hand,
            creator.move
        )
    }

    static class PlayerInGameCreator {
        Set<Card> hand = [] as Set
        Game.Move move
    }
}
