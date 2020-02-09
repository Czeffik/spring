package com.trzewik.spring.domain.player


import spock.lang.Specification

class PlayerFactoryUT extends Specification {

    def 'should create player with given name'() {
        given:
        def name = 'Adam'

        when:
        def player = PlayerFactory.createPlayer(name)

        then:
        player.@name == name
        player.@id != null
    }

    def 'should create croupier with generated id'() {
        when:
        def player = PlayerFactory.createCroupier()

        then:
        player.@name == 'Croupier'
        player.@id != null
    }

    def 'should create player with given name and id'() {
        given:
        def name = 'Adam'
        def id = '1231'

        when:
        def player = PlayerFactory.createPlayer(id, name)

        then:
        player.@name == name
        player.@id == id
    }

    def 'should throw exception when id is null'() {
        when:
        PlayerFactory.createPlayer(null, '')

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when name is null'() {
        when:
        PlayerFactory.createPlayer('', null)

        then:
        thrown(NullPointerException)
    }
}
