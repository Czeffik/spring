package com.trzewik.spring.interfaces.rest.player

trait PlayerControllerFormCreation {

    PlayerController.CreatePlayerForm createPlayerForm(CreatePlayerFormCreator creator = new CreatePlayerFormCreator()) {
        return new PlayerController.CreatePlayerForm(
            name: creator.name
        )
    }

    static class CreatePlayerFormCreator {
        String name = 'example name'
    }
}
