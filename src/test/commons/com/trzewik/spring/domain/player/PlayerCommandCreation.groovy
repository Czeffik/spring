package com.trzewik.spring.domain.player

trait PlayerCommandCreation {

    PlayerService.CreatePlayerCommand createCreatePlayerCommand(CreatePlayerCommandCreator creator = new CreatePlayerCommandCreator()) {
        return new PlayerService.CreatePlayerCommand(
            creator.name
        )
    }

    PlayerService.GetCroupierCommand createGetCroupierCommand(GetCroupierCommandCreator creator = new GetCroupierCommandCreator()) {
        return new PlayerService.GetCroupierCommand()
    }

    PlayerService.GetPlayerCommand createGetPlayerCommand(GetPlayerCommandCreator creator = new GetPlayerCommandCreator()) {
        return new PlayerService.GetPlayerCommand(
            creator.playerId
        )
    }

    PlayerService.GetPlayersCommand createGetPlayersCommand(GetPlayersCommandCreator creator = new GetPlayersCommandCreator()) {
        return new PlayerService.GetPlayersCommand(
            creator.playerIds
        )
    }

    static class CreatePlayerCommandCreator {
        String name = 'Adam'
    }

    static class GetCroupierCommandCreator {

    }

    static class GetPlayerCommandCreator {
        String playerId = 'iddd'
    }

    static class GetPlayersCommandCreator {
        List<String> playerIds = ['ias', 'asd']
    }
}
