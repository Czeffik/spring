package com.trzewik.spring.domain.player

trait PlayerFormCreation {

    PlayerService.CreateForm createPlayerForm(PlayerFormCreator creator = new PlayerFormCreator()) {
        return new PlayerService.CreateForm(
            name: creator.name
        )
    }

    static class PlayerFormCreator {
        String name = 'Adam'
    }
}
