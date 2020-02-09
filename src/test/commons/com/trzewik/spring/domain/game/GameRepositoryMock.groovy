package com.trzewik.spring.domain.game

class GameRepositoryMock implements GameRepository, GameCreation {
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
