package com.trzewik.spring.domain.player

import spock.lang.Specification
import spock.lang.Subject

class PlayerServiceImplUT extends Specification implements PlayerCreation {
    PlayerRepository repository = new PlayerRepositoryMock()

    @Subject
    PlayerService service = PlayerServiceFactory.create(repository)

    def 'should create new player with given name and save in repository'() {
        given:
        def playerName = 'Antoni'

        when:
        Player player = service.create(playerName)

        then:
        repository.repository.size() == 1
        player.name == playerName
    }

    def 'should get player from repository'() {
        given:
        Player player = createPlayer()

        and:
        repository.save(player)

        when:
        Player found = service.get(player.id)

        then:
        found == player
        found.name == player.name
    }

    def 'should throw exception when player is not found in repository'() {
        given:
        Player player = createPlayer()

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

    def 'should get player id from repository'() {
        given:
        Player player = createPlayer()

        and:
        repository.save(player)

        expect:
        service.getId(player.id) == player.id

    }

    def 'should throw exception when player id is not found in repository'() {
        given:
        Player player = createPlayer()

        and:
        repository.save(player)

        and:
        def otherId = player.id + 'a'

        when:
        service.getId(otherId)

        then:
        def ex = thrown(PlayerRepository.PlayerNotFoundException)
        ex.message == "Can not find player with id: [${otherId}] in repository."
    }

    def 'should create new croupier and save in repository'() {
        when:
        Player player = service.createCroupier()

        then:
        repository.repository.size() == 1
        player.name == 'Croupier'
    }

    def 'should create new croupier and save in repository and return croupier id'() {
        when:
        String croupierId = service.createCroupierAndGetId()

        then:
        with(repository.repository) {
            size() == 1
            get(croupierId)
        }
    }

    def 'should get players from repository'() {
        given:
        def players = createPlayers(4)

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

}
