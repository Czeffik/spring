package com.trzewik.spring.domain.player

import spock.lang.Specification
import spock.lang.Subject

class PlayerServiceImplUT extends Specification implements PlayerCreation, PlayerCommandCreation {
    PlayerRepository repository = new PlayerRepositoryMock()

    @Subject
    def service = PlayerServiceFactory.create(repository)

    def 'should create new player with given name and save in repository'() {
        given:
            def form = createPlayerCommand()

        when:
            def player = service.create(form)

        then:
            repository.repository.size() == 1

        and:
            player.name == form.name
            player.id

        and:
            repository.repository.get(player.id).name == form.name
    }

    def 'should get player from repository'() {
        given:
            def player = createPlayer()

        and:
            repository.save(player)

        when:
            def found = service.get(player.id)

        then:
            found == player
    }

    def 'should throw exception when player is not found in repository'() {
        given:
            def player = createPlayer()

        and:
            repository.save(player)

        and:
            def otherId = player.id + 'a'

        when:
            service.get(otherId)

        then:
            def ex = thrown(PlayerRepository.PlayerNotFoundException)
            ex.message == "Can not find player with id: [${otherId}] in repository."
    }

    def 'should create new croupier and save in repository'() {
        when:
            def player = service.getCroupier()

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

        when:
            def player = service.getCroupier()

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

        when:
            def found = service.get(players.collect { it.id })

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

    def 'should throw null pointer exception when player id is null when getting player'() {
        when:
            service.get(null as String)

        then:
            NullPointerException ex = thrown()
            ex.message == 'id is marked non-null but is null'
    }

    def 'should throw null pointer exception when player ids is null when getting players'() {
        when:
            service.get(null as List<String>)

        then:
            NullPointerException ex = thrown()
            ex.message == 'playerIds is marked non-null but is null'
    }

}
