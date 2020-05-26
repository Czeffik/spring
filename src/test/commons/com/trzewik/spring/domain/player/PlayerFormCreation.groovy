package com.trzewik.spring.domain.player

trait PlayerFormCreation {

    PlayerService.CreatePlayerCommand createPlayerForm(PlayerFormCreator creator = new PlayerFormCreator()) {
        return new PlayerService.CreatePlayerCommand(
            name: creator.name
        )
    }

    static class PlayerFormCreator {
        String name = 'Adam'
    }
}
