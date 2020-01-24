package com.trzewik.spring.domain.player

import spock.lang.Specification
import spock.lang.Subject

class PlayerServiceImplUT extends Specification implements PlayerCreation{
    PlayerRepository repository = new PlayerRepositoryMock()

    @Subject
    PlayerService service = PlayerServiceFactory.create(repository)

    def 'should create new player with given name and save in repository'(){
        given:
        def playerName = 'Antoni'

        when:
        Player player = service.createPlayer(playerName)

        then:
        repository.repository.size() == 1
        player.name == playerName
    }

    def 'should get player from repository'(){
        given:
        Player player = createPlayer()

        and:
        repository.save(player)

        when:
        Player found = service.getPlayer(player.id)

        then:
        found == player
        found.name == player.name
    }

    def 'should throw exception when player is not found in repository'(){
        given:
        Player player = createPlayer()

        and:
        repository.save(player)

        and:
        def otherId = player.id + 'a'

        when:
        service.getPlayer(otherId)

        then:
        def ex = thrown(PlayerRepository.PlayerNotFoundException)
        ex.message == "Can not find player with id: [${otherId}] in repository."
    }

}
