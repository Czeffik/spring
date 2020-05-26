package com.trzewik.spring.domain.player

trait PlayerCommandCreation {

    PlayerService.CreatePlayerCommand createPlayerCommand(PlayerCommandCreator creator = new PlayerCommandCreator()) {
        return new PlayerService.CreatePlayerCommand(
            creator.name
        )
    }

    static class PlayerCommandCreator {
        String name = 'Adam'
    }
}
