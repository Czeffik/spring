package com.trzewik.spring.domain.player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepo;

    @Override
    public Player createPlayer(String playerName) {
        Player player = PlayerFactory.createPlayer(playerName);

        playerRepo.save(player);

        return player;
    }

    @Override
    public Player getPlayer(String id) throws PlayerRepository.PlayerNotFoundException {
        return playerRepo.getById(id);
    }
}
