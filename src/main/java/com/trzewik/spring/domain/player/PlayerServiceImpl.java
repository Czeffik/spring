package com.trzewik.spring.domain.player;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepo;

    @Override
    public Player create(String playerName) {
        Player player = PlayerFactory.createPlayer(playerName);

        playerRepo.save(player);

        return player;
    }

    @Override
    public Player createCroupier() {
        Player croupier = PlayerFactory.createCroupier();

        playerRepo.save(croupier);

        return croupier;
    }

    @Override
    public Player get(String id) throws PlayerRepository.PlayerNotFoundException {
        return playerRepo.getById(id);
    }

    @Override
    public List<Player> get(List<String> playerIds) {
        return playerRepo.findAllById(playerIds);
    }
}
