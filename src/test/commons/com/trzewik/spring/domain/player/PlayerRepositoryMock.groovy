package com.trzewik.spring.domain.player

class PlayerRepositoryMock implements PlayerRepository, PlayerCreation {
    private final Map<String, Player> repository = new HashMap<>()

    @Override
    synchronized void save(Player player) {
        repository.put(player.getId(), createPlayer(new PlayerCreator(player)))
    }

    @Override
    synchronized Optional<Player> findById(String id) {
        Player player = repository.get(id)
        return Optional.ofNullable(player == null ? null : createPlayer(new PlayerCreator(player)))
    }

    @Override
    synchronized List<Player> findAllById(Collection<String> ids) {
        def players = []
        ids.each { players << createPlayer(new PlayerCreator(repository.get(it))) }
        return players
    }

    synchronized Map<String, Player> getRepository() {
        return repository
    }
}
