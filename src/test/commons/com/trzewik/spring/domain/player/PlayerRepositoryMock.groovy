package com.trzewik.spring.domain.player

class PlayerRepositoryMock implements PlayerRepository, PlayerCreation {
    private final Map<String, Player> repository = new HashMap<>()

    @Override
    synchronized void save(Player player) {
        repository.put(player.getId(), createPlayer(new PlayerCreator(player)))
    }

    @Override
    synchronized Optional<Player> findById(String id) {
        return Optional.ofNullable(repository.get(id))
    }

    @Override
    synchronized List<Player> findAllById(Collection<String> ids) {
        return ids.collect { repository.get(it) }
    }

    synchronized Map<String, Player> getRepository() {
        return repository
    }
}
