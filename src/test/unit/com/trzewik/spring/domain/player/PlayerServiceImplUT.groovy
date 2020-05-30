package com.trzewik.spring.domain.player

import spock.lang.Specification
import spock.lang.Subject

class PlayerServiceImplUT extends Specification implements PlayerCreation, PlayerCommandCreation {
    PlayerRepository repository = new PlayerRepositoryMock()

    @Subject
    def service = PlayerServiceFactory.create(repository)

    def 'should create new player with given name and save in repository'() {
        given:
            def command = createCreatePlayerCommand()
        when:
            def player = service.create(command)
        then:
            repository.repository.size() == 1
        and:
            player.name == command.name
            player.id
        and:
            repository.repository.get(player.id).name == command.name
    }

    def 'should get player from repository'() {
        given:
            def player = createPlayer()
        and:
            repository.save(player)
        and:
            def command = createGetPlayerCommand(new GetPlayerCommandCreator(playerId: player.id))
        when:
            def found = service.get(command)
        then:
            found == player
    }

    def 'should throw exception when player is not found in repository'() {
        given:
            def player = createPlayer()
        and:
            repository.save(player)
        and:
            def command = createGetPlayerCommand()
        when:
            service.get(command)
        then:
            def ex = thrown(PlayerRepository.PlayerNotFoundException)
            ex.message == "Can not find player with id: [${command.playerId}] in repository."
    }

    def 'should create new croupier and save in repository'() {
        given:
            def command = createGetCroupierCommand()
        when:
            def player = service.getCroupier(command)
        then:
            repository.repository.size() == 1
        and:
            player.name == 'CROUPIER'
            player.id == 'CROUPIER-ID'
        and:
            repository.repository.get('CROUPIER-ID').name == 'CROUPIER'
    }

    def 'should get croupier from repository'() {
        given:
            repository.save(createPlayer(PlayerCreator.croupier()))
        and:
            def command = createGetCroupierCommand()
        when:
            def player = service.getCroupier(command)
        then:
            repository.repository.size() == 1
        and:
            player.name == 'CROUPIER'
            player.id == 'CROUPIER-ID'
        and:
            repository.repository.get('CROUPIER-ID').name == 'CROUPIER'
    }

    def 'should get players from repository'() {
        given:
            def players = createPlayers()
        and:
            players.each { repository.save(it) }
        and:
            repository.save(createPlayer())
        and:
            def command = createGetPlayersCommand(new GetPlayersCommandCreator(playerIds: players.collect { it.id }))
        when:
            def found = service.get(command)
        then:
            found.size() == players.size()
            found == players
    }

    def 'should throw null pointer exception when createPlayerCommand is null'() {
        when:
            service.create(null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'createPlayerCommand is marked non-null but is null'
        and:
            repository.repository.isEmpty()
    }

    def 'should throw null pointer exception when create player command is null'() {
        when:
            service.create(null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'createPlayerCommand is marked non-null but is null'
    }

    def 'should throw null pointer exception when get croupier command is null'() {
        when:
            service.getCroupier(null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'getCroupierCommand is marked non-null but is null'
    }

    def 'should throw null pointer exception when get player command is null'() {
        when:
            service.get(null as PlayerService.GetPlayerCommand)
        then:
            NullPointerException ex = thrown()
            ex.message == 'getPlayerCommand is marked non-null but is null'
    }

    def 'should throw null pointer exception when get players command is null'() {
        when:
            service.get(null as PlayerService.GetPlayersCommand)
        then:
            NullPointerException ex = thrown()
            ex.message == 'getPlayersCommand is marked non-null but is null'
    }

    def 'should throw null pointer exception when name is null in create player command'() {
        when:
            createCreatePlayerCommand(new CreatePlayerCommandCreator(name: null))
        then:
            NullPointerException ex = thrown()
            ex.message == 'name is marked non-null but is null'
    }

    def 'should throw null pointer exception when player id is null in get player command'() {
        when:
            createGetPlayerCommand(new GetPlayerCommandCreator(playerId: null))
        then:
            NullPointerException ex = thrown()
            ex.message == 'playerId is marked non-null but is null'
    }

    def 'should throw null pointer exception when player ids are null in get players command'() {
        when:
            createGetPlayersCommand(new GetPlayersCommandCreator(playerIds: null))
        then:
            NullPointerException ex = thrown()
            ex.message == 'playerIds is marked non-null but is null'
    }

}
