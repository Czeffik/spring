package com.trzewik.spring.domain.game

class GameRepositoryMock implements GameRepository, GameCreation {
    private final Map<String, Game> repository = new HashMap<>()

    @Override
    synchronized void save(Game game) {
        repository.put(game.getId(), createGame(new GameCreator(game)))
    }

    @Override
    synchronized Optional<Game> findById(String id) {
        return Optional.ofNullable(repository.get(id))
    }

    @Override
    synchronized void update(Game game) {
        repository.replace(game.getId(), createGame(new GameCreator(game)))
    }

    synchronized Map<String, Game> getRepository() {
        return repository
    }
}
