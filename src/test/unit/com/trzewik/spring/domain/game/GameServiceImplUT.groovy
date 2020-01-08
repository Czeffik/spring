package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.common.Deck
import com.trzewik.spring.domain.common.PlayerGameRepository
import com.trzewik.spring.domain.result.Result
import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerCreation
import com.trzewik.spring.domain.player.PlayerRepository
import groovy.transform.EqualsAndHashCode
import spock.lang.Specification
import spock.lang.Subject

class GameServiceImplUT extends Specification implements GameCreation {

    GameRepository gameRepo = new GameRepositoryMock()
    PlayerRepository playerRepo = new PlayerRepositoryMock()
    PlayerGameRepository playerGameRepo = new PlayerGameRepositoryMock()

    @Subject
    GameService service = GameServiceFactory.create(gameRepo, playerRepo, playerGameRepo)

    def 'should create game with croupier and deck and save in repositories'() {
        when:
        Game savedGame = service.createGame()

        then:
        gameRepo.repository.size() == 1
        playerRepo.repository.size() == 1
        playerGameRepo.repository.size() == 1
        gameRepo.findById(savedGame.id).get() == savedGame
        playerRepo.findById(savedGame.croupier.id).get() == savedGame.croupier
        playerGameRepo.findById(savedGame.id, savedGame.croupier.id).get() == savedGame.croupier
    }

    def 'should add new player to game with gameId and save player in player repositories'() {
        given:
        Game game = service.createGame()

        when:
        Player createdPlayer = service.addPlayer(game.id, 'Adam')

        then:
        playerRepo.repository.size() == 2
        game.players.size() == 1
        game.players.first() == createdPlayer
        playerRepo.findById(createdPlayer.id).get() == createdPlayer
        playerGameRepo.repository.size() == 2
        playerGameRepo.findById(game.id, createdPlayer.id).get() == createdPlayer
    }

    def 'should NOT add player to repository and throw exception when can not find game with gameId'() {
        when:
        service.addPlayer('wrong-game-id', 'Adam')

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'

        and:
        playerRepo.repository.isEmpty()
        playerGameRepo.repository.isEmpty()
    }

    def 'should NOT add player to repository and throw exception when trying add player to game which is started'() {
        given:
        Game game = service.createGame()
        service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        service.addPlayer(game.id, 'Niet')

        then:
        thrown(GameException)

        and:
        gameRepo.repository.size() == 1
        playerRepo.repository.size() == 2
        playerGameRepo.repository.size() == 2
    }

    def '''should find game by id in repository and start it by
        shuffling deck, dealing cards, setting currentPlayer and game status to STARTED'''() {
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')

        when:
        def returned = service.startGame(game.id)

        then:
        with(gameRepo.findById(game.id).get()) {
            status == Game.Status.STARTED
            deck.@cards.size() == 48
            currentPlayer == player
        }
        playerGameRepo.findById(game.id, player.id).get().hand.size() == 2
        playerGameRepo.findById(game.id, game.croupier.id).get().hand.size() == 2
        playerGameRepo.repository.size() == 2
        playerRepo.repository.size() == 2
        returned != null
    }

    def 'should NOT start game and throw exception when can not find game with gameId'() {
        when:
        service.startGame('wrong-game-id')

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'
    }

    def 'should NOT start game when no players added to game and throw exception'() {
        given:
        Game game = service.createGame()

        when:
        service.startGame(game.id)

        then:
        thrown(GameException)

        and:
        game.status == Game.Status.NOT_STARTED

        and:
        gameRepo.findById(game.id).get().status == Game.Status.NOT_STARTED
        playerGameRepo.repository.size() == 1
        playerGameRepo.findById(game.id, game.croupier.id).get().hand.size() == 0
    }

    def 'should throw exception when trying start game which is already started'() {
        given:
        Game game = service.createGame()
        service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        service.startGame(game.id)

        then:
        thrown(GameException)
    }

    def '''should find game and player in repository and player should make move HIT (draw card from deck)
         when game is started and update repositories'''() {
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        Game returnedGame = service.makeMove(game.id, player.id, Game.Move.HIT)

        then:
        with(gameRepo.findById(game.id).get()) {
            deck.@cards.size() <= 47        //less because can have more than 21 points and it will finish the game
        }
        playerGameRepo.findById(game.id, player.id).get().hand.size() == 3

        and:
        returnedGame != null
    }

    def 'should throw exception when can not find game with gameId in game repository when trying make move'() {
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        service.makeMove('wrong-game-id', player.id, Game.Move.HIT)

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'
    }

    def 'should not be possible to make move when game was not started'() {
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')

        when:
        service.makeMove(game.id, player.id, Game.Move.HIT)

        then:
        thrown(GameException)

        and:
        playerGameRepo.findById(game.id, player.id).get().hand.size() == 0
    }

    def 'should end game when last player move was STAND and should not be possible to make next move'() {
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        service.makeMove(game.id, player.id, Game.Move.STAND)

        then:
        with(playerGameRepo.findById(game.id, player.id).get()) {
            hand.size() == 2
            move == Game.Move.STAND
        }
        gameRepo.findById(game.id).get().status == Game.Status.ENDED
        playerGameRepo.findById(game.id, game.croupier.id).get().move == Game.Move.STAND

        when:
        service.makeMove(game.id, player.id, Game.Move.HIT)

        then:
        thrown(GameException)

        and:
        with(playerGameRepo.findById(game.id, player.id).get()) {
            hand.size() == 2
            move == Game.Move.STAND
        }
    }

    def 'should find game in repository and return game results when game is ended'() {
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)
        service.makeMove(game.id, player.id, Game.Move.STAND)

        when:
        List<Result> results = service.getGameResults(game.id)

        then:
        results.size() == 2
    }

    def 'should throw exception when can not find game in repository with gameId when getting results'() {
        when:
        service.getGameResults('wrong-game-id')

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'
    }

    def 'should not be possible get results from game which is not ended'() {
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        service.getGameResults(game.id)

        then:
        thrown(GameException)
    }

    static class GameRepositoryMock implements GameRepository, GameCreation {
        private final Map<String, Game> repository = new HashMap<>()

        @Override
        synchronized void save(Game game) {
            repository.put(game.getId(), createGame(new GameBuilder(game)))
        }

        @Override
        synchronized Optional<Game> findById(String id) {
            Game game = repository.get(id)
            return Optional.ofNullable(game == null ? game : createGame(new GameBuilder(game)))
        }

        @Override
        synchronized void update(Game game) {
            repository.put(game.getId(), createGame(new GameBuilder(game)))
        }

        synchronized Map<String, Game> getRepository() {
            return repository
        }
    }

    static class PlayerRepositoryMock implements PlayerRepository, PlayerCreation {
        private final Map<String, Player> repository = new HashMap<>()

        @Override
        synchronized void save(Player player) {
            repository.put(player.getId(), createPlayer(new PlayerBuilder(player)))
        }

        @Override
        synchronized Optional<Player> findById(String id) {
            Player player = repository.get(id)
            return Optional.ofNullable(player == null ? player : createPlayer(new PlayerBuilder(player)))
        }

        synchronized Map<String, Player> getRepository() {
            return repository
        }
    }

    static class PlayerGameRepositoryMock implements PlayerGameRepository, PlayerCreation {
        private final Map<PlayerGameIdMock, PlayerGameMock> repository = new HashMap<>()

        @Override
        synchronized void save(Player player, String gameId) {
            def id = new PlayerGameIdMock(playerId: player.getId(), gameId: gameId)
            repository.put(
                id,
                new PlayerGameMock(
                    id: id,
                    move: Game.Move.valueOf(player.getMove().name()),
                    hand: new ArrayList(player.getHand()),
                    player: createPlayer(new PlayerBuilder(player))
                )
            )
        }

        @Override
        synchronized Optional<Player> findById(String gameId, String playerId) {
            def id = new PlayerGameIdMock(playerId: playerId, gameId: gameId)
            PlayerGameMock playerGame = repository.get(id)
            return Optional.ofNullable(
                playerGame.player == null ?
                    null :
                    createPlayer(new PlayerBuilder(
                        id: id.playerId,
                        move: playerGame.move,
                        hand: playerGame.hand,
                        name: playerGame.player.name
                    )))
        }

        @Override
        synchronized void update(Player player, String gameId) {
            def id = new PlayerGameIdMock(playerId: player.getId(), gameId: gameId)
            repository.put(
                id,
                new PlayerGameMock(
                    id: id,
                    move: Game.Move.valueOf(player.getMove().name()),
                    hand: new ArrayList(player.getHand()),
                    player: createPlayer(new PlayerBuilder(player))
                )
            )
        }

        synchronized Map<PlayerGameIdMock, PlayerGameMock> getRepository() {
            return repository
        }

        @EqualsAndHashCode
        static class PlayerGameIdMock implements Serializable {
            String playerId
            String gameId
        }

        @EqualsAndHashCode
        static class PlayerGameMock {
            PlayerGameIdMock id
            Player player
            Game.Move move
            Set<Deck.Card> hand
        }
    }
}
